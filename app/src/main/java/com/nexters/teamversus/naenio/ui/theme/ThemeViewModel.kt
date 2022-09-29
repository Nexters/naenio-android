package com.nexters.teamversus.naenio.ui.theme

import com.nexters.teamversus.naenio.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor() : BaseViewModel() {
    val themeItems = MutableStateFlow<List<ThemeItem>>(getThemeItems()).asStateFlow()

    private fun getThemeItems(): List<ThemeItem> {
        return ThemeItem.themeList
    }
}