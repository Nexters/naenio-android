package com.nexters.teamversus.naenio.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import com.nexters.teamversus.naenio.extensions.fragmentComposeView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return fragmentComposeView.apply {
            setContent {
                HomeScreen()
            }
        }
    }

    @Composable
    fun HomeScreen() {
        Box(modifier = Modifier.background(Color.Yellow))
    }
}