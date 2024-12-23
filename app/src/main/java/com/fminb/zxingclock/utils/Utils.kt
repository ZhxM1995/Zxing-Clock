package com.fminb.zxingclock.utils

import android.util.TypedValue
import com.fminb.zxingclock.MyApplication

object Utils {
    fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, MyApplication.getAppContext().resources.displayMetrics)
    }
}