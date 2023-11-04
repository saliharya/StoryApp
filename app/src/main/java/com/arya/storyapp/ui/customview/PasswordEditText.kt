package com.arya.storyapp.ui.customview

import android.content.Context
import android.util.AttributeSet

class PasswordEditText(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatEditText(context, attrs) {
    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        error = if ((text?.length ?: 0) < 8) {
            "Password must be at least 8 characters long."
        } else {
            null
        }
    }
}
