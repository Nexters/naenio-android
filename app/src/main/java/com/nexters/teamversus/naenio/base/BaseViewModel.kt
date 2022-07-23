package com.nexters.teamversus.naenio.base

import android.util.Log
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    val className = this.javaClass.name + " ### "

    override fun onCleared() {
        super.onCleared()
        Log.d(className, "++onCleared()")
    }
}