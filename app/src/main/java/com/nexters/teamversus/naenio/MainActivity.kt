package com.nexters.teamversus.naenio

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import com.nexters.teamversus.naenio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getHashKey()
    }

    private fun getHashKey() {
        val keyHash = Utility.getKeyHash(this)
        Log.i("++ KeyHash" , keyHash)
    }
}