package org.cookpad.app_test.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeableViewPager(context: Context, attributeSet: AttributeSet? = null) : ViewPager(context, attributeSet) {
    override fun onTouchEvent(ev: MotionEvent?) = false
    override fun onInterceptTouchEvent(ev: MotionEvent?) = false
}