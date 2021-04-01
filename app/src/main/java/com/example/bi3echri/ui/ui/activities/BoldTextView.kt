package com.example.bi3echri.ui.ui.activities

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class BoldTextView (context: Context, attributesSet:AttributeSet)
    : AppCompatTextView (context, attributesSet){

    init {
    applyfont();
    }
    private fun applyfont()
    {
        val boldTypeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface=boldTypeface

    }
}