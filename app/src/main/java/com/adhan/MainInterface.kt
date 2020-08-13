package com.adhan

import android.view.View

interface MainInterface {
    fun goToAudioFragment()
    fun goToHomeFragment()
    fun goToSettingsFragment()
    fun goToMapFragment()
}
interface HomeInterface {
    fun goHomeButtonChecked()
}

interface CellClickListener {
    fun onCellClickListener(it: View)
    fun onButtonClickListener(it: View)
}