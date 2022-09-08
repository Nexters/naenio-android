package com.nexters.teamvs.naenio.utils.keyboard

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import androidx.lifecycle.*
import com.nexters.teamvs.naenio.R

/**
 * The keyboard height provider, this class uses a PopupWindow
 * to calculate the window height when the floating keyboard is opened and closed.
 *
 * https://github.com/siebeprojects/samples-keyboardheight 에서 필요한 로직만 가져오고 코틀린으로 변환한 버전입니다.
 * 빈 윈도우를 띄워서 해당 윈도우가 받는 layout이벤트를 받아서 전달하는 로직입니다.
 * 사용하는 액티비티 설정에 상관 없이 동작합니다.
 */
class KeyboardAwareWindow(private val activity: Activity) : PopupWindow(activity), DefaultLifecycleObserver {
    var onKeyboardChangedEvent: ((height: Int, orientation: Int) -> Unit)? = null

    private var keyboardLandscapeHeight = 0
    private var keyboardPortraitHeight = 0

    /** The view that is used to calculate the keyboard height  */
    private val popupView: View?

    /** The parent view  */
    private val parentView: View

    /**
     * Start the KeyboardHeightProvider, this must be called after the onResume of the Activity.
     * PopupWindows are not allowed to be registered before the onResume has finished
     * of the Activity.
     */

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        start()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        start()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        start()
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        close()
    }

    fun start() {
        if (!isShowing && parentView.windowToken != null) {
            setBackgroundDrawable(ColorDrawable(0))
            showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0)
        }
    }

    /**
     * Close the keyboard height provider,
     * this provider will not be used anymore.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun close() {
        onKeyboardChangedEvent = null
        dismiss()
    }

    private var lastY = 0
    /**
     * Popup window itself is as big as the window of the Activity.
     * The keyboard can then be calculated by extracting the popup view bottom
     * from the activity window height.
     */
    private fun handleOnGlobalLayout() {
        val screenSize = Point()
        activity.windowManager.defaultDisplay.getSize(screenSize)

        val windowRect = Rect()
        popupView?.getWindowVisibleDisplayFrame(windowRect)

        // REMIND, you may like to change this using the fullscreen size of the phone
        // and also using the status bar and navigation bar heights of the phone to calculate
        // the keyboard height. But this worked fine on a Nexus.
        val orientation = screenOrientation

        if (lastY == 0) {
            lastY = windowRect.bottom
        }

        val keyboardHeight: Int = lastY - windowRect.bottom
        Log.d("###keyboardHeight Util2", "$keyboardHeight")
        when {
            keyboardHeight == 0 -> {
                notifyKeyboardHeightChanged(0, orientation)
            }
            orientation == Configuration.ORIENTATION_PORTRAIT -> {
                keyboardPortraitHeight = keyboardHeight
                notifyKeyboardHeightChanged(keyboardPortraitHeight, orientation)
            }
            else -> {
                keyboardLandscapeHeight = keyboardHeight
                notifyKeyboardHeightChanged(keyboardLandscapeHeight, orientation)
            }
        }
    }

    private val screenOrientation: Int
        get() = activity.resources.configuration.orientation

    private fun notifyKeyboardHeightChanged(height: Int, orientation: Int) {
        if (onKeyboardChangedEvent != null) {
            onKeyboardChangedEvent?.invoke(height, orientation)
        }
    }

    /**
     * Construct a new KeyboardHeightProvider
     *
     * @param activity The parent activity
     */
    init {
        val inflater =
            activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        popupView = inflater.inflate(R.layout.keyboard_aware_window, null, false)

        contentView = popupView
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = INPUT_METHOD_NEEDED

        parentView = activity.window.decorView

        width = 0
        height = WindowManager.LayoutParams.MATCH_PARENT

        popupView.viewTreeObserver
            .addOnGlobalLayoutListener {
                Log.d("###", "addOnGlobalLayoutListener")
                if (popupView != null) {
                    handleOnGlobalLayout()
                }
            }
    }
}

val Activity.keyboardHeight: LiveData<Int>
    get() {
        val heightLiveData = MutableLiveData(0)

        val window = KeyboardAwareWindow(this).apply {
            onKeyboardChangedEvent = { height: Int, _: Int ->
                heightLiveData.postValue(height)
            }
        }

//        this.lifecycle.addObserver(window)
        return heightLiveData
    }