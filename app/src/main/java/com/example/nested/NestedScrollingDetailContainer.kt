package com.example.nested

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat

class NestedScrollingDetailContainer(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs), NestedScrollingParent {
    private var mParentHelper: NestedScrollingParentHelper? = null

    private fun getNestedScrollingHelper(): NestedScrollingParentHelper {
        if (mParentHelper == null) {
            mParentHelper = NestedScrollingParentHelper(this)
        }
        return mParentHelper!!

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return (nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun getNestedScrollAxes(): Int {
        return getNestedScrollingHelper().nestedScrollAxes
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        getNestedScrollingHelper().onNestedScrollAccepted(child, target, axes)
    }

    override fun onStopNestedScroll(child: View) {
        getNestedScrollingHelper().onStopNestedScroll(child)
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return false
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
//        super.onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
//        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }
}