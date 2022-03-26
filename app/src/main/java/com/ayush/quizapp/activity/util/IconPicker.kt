package com.ayush.quizapp.activity.util

import com.ayush.quizapp.R

object IconPicker {
    private val icons = arrayOf(
        R.drawable.ic_icon01,
        R.drawable.ic_icon02,
        R.drawable.ic_icon03,
        R.drawable.ic_icon04,
        R.drawable.ic_icon05,
        R.drawable.ic_icon06
    )
    var currentIconIndex = 1
fun getIcons(): Int{
    currentIconIndex = (currentIconIndex+1)%icons.size
    return icons[currentIconIndex]
}
}