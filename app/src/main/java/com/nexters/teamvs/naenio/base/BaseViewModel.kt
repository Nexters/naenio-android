package com.nexters.teamvs.naenio.base

import android.util.Log
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    val className = this.javaClass.name + " ### "

    init {
        Log.d(className, "++init() $this")
    }
    override fun onCleared() {
        super.onCleared()
        Log.d(className, "++onCleared()")
    }
}