package com.example.bi3echri.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class MSPRadioButton (context: Context, attributeSet: AttributeSet):AppCompatRadioButton (context,attributeSet){
    init {
        applyFront()
    }
    private fun applyFront()
    {
        var typeface: Typeface =
            Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}

