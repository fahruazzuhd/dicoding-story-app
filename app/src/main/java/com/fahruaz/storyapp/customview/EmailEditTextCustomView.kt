package com.fahruaz.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EmailEditTextCustomView : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //Do Nothing
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
                    error = "Masukkan format email yang valid"
                }
            }
            override fun afterTextChanged(s: Editable) {
                //Do Nothing
            }
        })
    }
}