package com.example.nested

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.WebView
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper

class NestedScrollingWebView(context: Context, attrs: AttributeSet?) : WebView(context, attrs), NestedScrollingChild {
    private var mChildHelper: NestedScrollingChildHelper? = null
    private fun getNestedScrollingChildHelper(): NestedScrollingChildHelper {
        if (mChildHelper == null) {
            mChildHelper = NestedScrollingChildHelper(this)
        }
        return mChildHelper!!
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
//        super.setNestedScrollingEnabled(enabled)
        getNestedScrollingChildHelper().isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return getNestedScrollingChildHelper().isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return getNestedScrollingChildHelper().startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
//        super.stopNestedScroll()
        getNestedScrollingChildHelper().stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return getNestedScrollingChildHelper().hasNestedScrollingParent()
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return getNestedScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean {
        return getNestedScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return getNestedScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return getNestedScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed)
    }

//    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
//        return getNestedScrollingChildHelper().onStartNestedScroll(child, target, nestedScrollAxes)
//    }

    override fun onStopNestedScroll(child: View) {
        getNestedScrollingChildHelper().onStopNestedScroll(child)
    }



}