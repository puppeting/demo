package com.inclusive.finance.interfaces

import android.view.View

interface PresenterClick : View.OnClickListener {
    override fun onClick(v: View?)
}

