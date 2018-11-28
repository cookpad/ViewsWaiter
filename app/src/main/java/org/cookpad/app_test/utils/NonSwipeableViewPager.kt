package org.cookpad.app_test.utils

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NonSwipeableViewPager(context: Context, attributeSet: AttributeSet? = null) : androidx.viewpager.widget.ViewPager(context, attributeSet) {
    override fun onTouchEvent(ev: MotionEvent?) = false
    override fun onInterceptTouchEvent(ev: MotionEvent?) = false
}