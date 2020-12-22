package com.example.bi3echri.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MSPButton (context: Context,attrs:AttributeSet):AppCompatButton(context,attrs)
{
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