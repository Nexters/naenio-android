package com.nexters.teamversus.naenio.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Vaccines
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import com.nexters.teamversus.naenio.extensions.fragmentComposeView

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return fragmentComposeView.apply {
            setContent {

            }
        }
    }

    @Composable
    fun MainScreen() {

    }


    @Composable
    fun BottomNavigationBar() {
        Row(Modifier.background(Color.LightGray)) {
            Image(
                modifier = Modifier.weight(1f),
                imageVector = Icons.Rounded.Vaccines,
                contentDescription = null
            )
            Image(
                modifier = Modifier.weight(1f),
                imageVector = Icons.Rounded.Vaccines,
                contentDescription = null
            )
            Image(
                modifier = Modifier.weight(1f),
                imageVector = Icons.Rounded.Vaccines,
                contentDescription = null
            )
        }
    }
}
