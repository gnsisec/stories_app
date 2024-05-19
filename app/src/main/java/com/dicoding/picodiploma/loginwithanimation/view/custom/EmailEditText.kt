package com.dicoding.picodiploma.loginwithanimation.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val emailValidationFormat = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.com\$"

        if ( s.toString().isNotEmpty() && !s.matches(emailValidationFormat.toRegex()) ) {
            setError("Email yang anda masukan tidak sesuai format", null)
        } else {
            error = null
        }
    }
}