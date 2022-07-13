package com.nexters.teamversus.naenio

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.util.Utility
import com.nexters.teamversus.naenio.data.network.ApiProvider
import com.nexters.teamversus.naenio.data.network.api.NaenioApi
import com.nexters.teamversus.naenio.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getHashKey()
        test()
    }

    private fun getHashKey() {
        val keyHash = Utility.getKeyHash(this)
        Log.i("++ KeyHash" , keyHash)
    }

    private fun test() {
        lifecycleScope.launch {
            ApiProvider.retrofit.create(NaenioApi::class.java).getData().also {
                Log.d("###", it.toString())
            }
        }
    }

}