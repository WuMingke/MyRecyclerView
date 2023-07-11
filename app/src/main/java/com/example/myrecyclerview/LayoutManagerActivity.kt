package com.example.myrecyclerview

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class LayoutManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_layoutmanager)

        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.adapter = LayoutAdapter(DataUtils.getData())
//        rv.layoutManager = MyLayoutManager(rv.context, LinearLayoutManager.HORIZONTAL, false)
        rv.layoutManager = CustomLayoutManager()
    }
}


// practice 简易的demo
class CustomLayoutManager : LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    private var mTotalHeight = 0
    override fun onLayoutChildren(recycler: Recycler?, state: RecyclerView.State?) {
        var offsetY = 0
        for (i in 0 until itemCount) {
            val view = recycler?.getViewForPosition(i) ?: break
            addView(view)
            measureChildWithMargins(view, 0, 0)
            val width = getDecoratedMeasuredWidth(view)
            val height = getDecoratedMeasuredHeight(view)
            layoutDecorated(view, 0, offsetY, width, offsetY + height)
            offsetY += height
        }

        mTotalHeight = maxOf(offsetY, getVerticalSpace())
    }

    private fun getVerticalSpace(): Int {
        return height - paddingBottom - paddingTop
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    private var mSumDy = 0
    override fun scrollVerticallyBy(dy: Int, recycler: Recycler?, state: RecyclerView.State?): Int {
        var travel = dy
        // 判断到顶部，不能再滑动
        if (mSumDy + dy < 0) {
            travel = -mSumDy
        } else if (mSumDy + dy > mTotalHeight - getVerticalSpace()) { // 判断到底
            travel = mTotalHeight - getVerticalSpace() - mSumDy
        }
        mSumDy += travel
        offsetChildrenVertical(-travel)
        return dy
    }


}

// 这个LayoutManager，依次看每个方法，体现了流程
class MyLayoutManager2(private val orientation: Int) : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return if (orientation == HORIZONTAL) {
            RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT
            )
        } else {
            RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onMeasure(recycler: Recycler, state: RecyclerView.State, widthSpec: Int, heightSpec: Int) {
        if (state.itemCount == 0) {
            super.onMeasure(recycler, state, widthSpec, heightSpec)
            return
        }
        if (state.isPreLayout) return
        // 假定每个item的宽高一直，所以用第一个View计算宽高
        // 这种方式可能不太好
        val itemView = recycler.getViewForPosition(0)
        addView(itemView)
        // 这里不能用measureChild方法，具体看内部实现，内部getWidth默认为0
        itemView.measure(widthSpec, heightSpec)
        val mItemWidth = getDecoratedMeasuredWidth(itemView)
        val mItemHeight = getDecoratedMeasuredHeight(itemView)
        //回收
        detachAndScrapView(itemView, recycler)

        //设置宽高 。。。这里是业务的，不一定调用
        setWidthAndHeight(mItemWidth, mItemHeight)
    }

    private fun setWidthAndHeight(width: Int, height: Int) {
//        if (orientation == HORIZONTAL) {
//            setMeasuredDimension(width * visibleCount, height)
//        } else {
//            setMeasuredDimension(width, height * visibleCount)
//        }
    }

    override fun onLayoutChildren(recycler: Recycler?, state: RecyclerView.State?) {
//        super.onLayoutChildren(recycler, state)
        // 1 计算剩余空间
        // 2 addView()
        // 3 measureView()
        // 4 layoutView()
        //以下是伪代码
        // 轻量级地将view移出屏幕
        if (recycler != null) {
            detachAndScrapAttachedViews(recycler)
        }
        // 开始填充
        var totalSpace = width - paddingRight
        var currentPosition = 0
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0
        // 模仿LinearLayoutManager的写法，当可用距离足够和要填充的itemView
        // 的position的合法范围内才填充View
        while (totalSpace > 0 && currentPosition < (state?.itemCount ?: 0)) {
            val view = recycler?.getViewForPosition(currentPosition) ?: break
            addView(view)
            measureChild(view, 0, 0)

            right = left + getDecoratedMeasuredWidth(view)
            bottom = top + getDecoratedMeasuredHeight(view)
            layoutDecorated(view, left, top, right, bottom)
            currentPosition++
            left += getDecoratedMeasuredWidth(view)
            totalSpace -= getDecoratedMeasuredWidth(view)
        }

    }

    // 支持滑动
    override fun canScrollHorizontally(): Boolean {
        return orientation == HORIZONTAL
    }

    override fun canScrollVertically(): Boolean {
        return orientation == VERTICAL
    }

    // 滑动回收
    override fun scrollVerticallyBy(dy: Int, recycler: Recycler?, state: RecyclerView.State?): Int {
        return super.scrollVerticallyBy(dy, recycler, state)
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler?, state: RecyclerView.State?): Int {
        // 填充,consumed就是修复后的移动值
        val consumed = fill(dx, recycler)
        // 移动View
        offsetChildrenHorizontal(-consumed)
        // 回收
        recycle(consumed, recycler)
        return consumed
    }

    private fun fill(dx: Int, recycler: Recycler?): Int {
        // 1 获取锚点View的position
        // 2 计算新的锚点View的position和位置
        // 3 addView
        // 4 measureView
        // 5 layoutView

        //将要填充的position
        var fillPosition = RecyclerView.NO_POSITION
        //可用的空间，和onLayoutChildren中的totalSpace类似
        var availableSpace = abs(dx)
        //增加一个滑动距离的绝对值，方便计算
        val absDelta = abs(dx)
        //将要填充的View的左上右下
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0


        //dx>0就是手指从右滑向左，所以就要填充尾部
        if (dx > 0) {
            val anchorView = getChildAt(childCount - 1)!!
            val anchorPosition = getPosition(anchorView)
            val anchorRight = getDecoratedRight(anchorView)

            left = anchorRight
            //填充尾部，那么下一个position就应该是+1
            fillPosition = anchorPosition + 1

            //如果要填充的position超过合理范围并且最后一个View的
            //right-移动的距离 < 右边缘(width)那就要修正真实能移动的距离
            if (fillPosition >= itemCount && anchorRight - absDelta < width) {
                val fixScrolled = anchorRight - width
                Log.d("scrollHorizontallyBy", "fill == $fixScrolled")
                return fixScrolled
            }

            //如果尾部的锚点位置减去dx还是在屏幕外，就不填充下一个View
            if (anchorRight - absDelta > width) {
                return dx
            }
        }

        //dx<0就是手指从左滑向右，所以就要填充头部
        if (dx < 0) {
            val anchorView = getChildAt(0)!!
            val anchorPosition = getPosition(anchorView)
            val anchorLeft = getDecoratedLeft(anchorView)

            right = anchorLeft
            //填充头部，那么上一个position就应该是-1
            fillPosition = anchorPosition - 1

            //如果要填充的position超过合理范围并且第一个View的
            //left+移动的距离 > 左边缘(0)那就要修正真实能移动的距离
            if (fillPosition < 0 && anchorLeft + absDelta > 0) {
                return anchorLeft
            }

            //如果头部的锚点位置加上dx还是在屏幕外，就不填充上一个View
            if (anchorLeft + absDelta < 0) {
                return dx
            }
        }

        //根据限定条件，不停地填充View进来
        while (availableSpace > 0 && (fillPosition in 0 until itemCount)) {
            val itemView = recycler?.getViewForPosition(fillPosition) ?: break

            if (dx > 0) {
                addView(itemView)
            } else {
                addView(itemView, 0)
            }

            measureChild(itemView, 0, 0)

            if (dx > 0) {
                right = left + getDecoratedMeasuredWidth(itemView)
            } else {
                left = right - getDecoratedMeasuredWidth(itemView)
            }

            bottom = top + getDecoratedMeasuredHeight(itemView)
            layoutDecorated(itemView, left, top, right, bottom)

            if (dx > 0) {
                left += getDecoratedMeasuredWidth(itemView)
                fillPosition++
            } else {
                right -= getDecoratedMeasuredWidth(itemView)
                fillPosition--
            }

            if (fillPosition in 0 until itemCount) {
                availableSpace -= getDecoratedMeasuredWidth(itemView)
            }
        }

        return dx
    }


    private fun recycle(dx: Int, recycler: Recycler?) {
        // 要回收View的集合，暂存 。。。 这里感觉不太合理，去看看源码
        val recyclerViews = hashSetOf<View>()
        // 从右往左，回收前面的
        if (dx > 0) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)!!
                val right = getDecoratedRight(child)
                if (right > 0) break
                recyclerViews.add(child)
            }
        }
        // 从左向右，回收后面的
        if (dx < 0) {
            for (i in childCount - 1 downTo 0) {
                val child = getChildAt(i)!!
                val left = getDecoratedLeft(child)
                //itemView的left>recyclerView.width就是要超出屏幕要回收View
                if (left < width) break
                recyclerViews.add(child)
            }
        }
        for (view in recyclerViews) {
            recycler?.let { removeAndRecycleView(view, it) }
        }
        recycler?.clear()
    }

}

// 自定义无限循环的 LayoutManager
class MyLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) : LinearLayoutManager(context, orientation, reverseLayout) {

    // TODO: 2023/7/8 mingKE need override
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
//        return super.generateDefaultLayoutParams()
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally()
    }

    // TODO: 2023/7/8 mingKE need override
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
//        super.onLayoutChildren(recycler, state)
        if (itemCount <= 0) return
        if (state?.isPreLayout == true) return

        // 将所有Item分离至scrap
        recycler?.apply {
            detachAndScrapAttachedViews(this)
        }

        var itemLeft = paddingLeft
        for (i in 0..itemCount) {
            if (itemLeft >= width - paddingRight) {
                break
            }
            val itemView = recycler?.getViewForPosition(i % itemCount) ?: break
            // 添加子View
            addView(itemView)
            // 测量子View
            measureChildWithMargins(itemView, 0, 0)

            val right = itemLeft + getDecoratedMeasuredWidth(itemView)
            val top = paddingTop
            val bottom = top + getDecoratedMeasuredHeight(itemView) - paddingBottom
            // 对子View进行布局
            layoutDecorated(itemView, itemLeft, top, right, bottom)
            itemLeft = right
        }

        Log.i(DataUtils.TAG, "childCount: ${childCount}")
        Log.i(DataUtils.TAG, "itemCount: ${itemCount}")
    }

    // TODO: 2023/7/8 mingKE need override
    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
//        return super.scrollHorizontallyBy(dx, recycler, state)
        fill(recycler, dx > 0) // key1
        offsetChildrenHorizontal(-dx) // key2
        recyclerChildView(dx > 0, recycler) // key3
        return dx
    }

    // 滑动的时候 填充可见的未填充区域
    private fun fill(recycler: Recycler?, fillEnd: Boolean) {
        if (childCount == 0) return
        if (fillEnd) {
            // 尾部
            var anchorView = getChildAt(childCount - 1) ?: return
            val anchorPosition = getPosition(anchorView)
            while (anchorView.right < width - paddingRight) {
                var position = (anchorPosition + 1) % itemCount
                if (position < 0) {
                    position += itemCount;
                }
                val scrapItem = recycler?.getViewForPosition(position) ?: return
                addView(scrapItem)
                measureChildWithMargins(scrapItem, 0, 0)

                val left = anchorView.right
                val top = paddingTop
                val right = left + getDecoratedMeasuredWidth(scrapItem)
                val bottom = top + getDecoratedMeasuredHeight(scrapItem) - paddingBottom
                layoutDecorated(scrapItem, left, top, right, bottom)
                anchorView = scrapItem
            }
        } else {
            // 首部
            var anchorView = getChildAt(0) ?: return
            val anchorPosition = getPosition(anchorView)
            while (anchorView.left > paddingLeft) {
                var position = (anchorPosition - 1) % itemCount
                if (position < 0) {
                    position += itemCount
                }
                val scrapItem = recycler?.getViewForPosition(position) ?: return
                addView(scrapItem, 0)
                measureChildWithMargins(scrapItem, 0, 0)
                val right = anchorView.left
                val top = paddingTop
                val left = right - getDecoratedMeasuredWidth(scrapItem)
                val bottom = top + getDecoratedMeasuredHeight(scrapItem) - paddingBottom
                layoutDecorated(scrapItem, left, top, right, bottom)
                anchorView = scrapItem
            }
        }
    }

    // 回收不可见的子View
    private fun recyclerChildView(fillEnd: Boolean, recycler: Recycler?) {
        if (fillEnd) {
            // 回收头部
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                val needRecycler = view != null && view.right < paddingLeft
                if (needRecycler && view != null && recycler != null) {
                    removeAndRecycleView(view, recycler)
                } else {
                    return // 为什么是return呢
                }
            }
        } else {
            // 回收尾部
            for (i in childCount - 1 downTo 0) {
                val view = getChildAt(i)
                val needRecycler = view != null && view.left > width - paddingRight
                if (needRecycler && view != null && recycler != null) {
                    removeAndRecycleView(view, recycler)
                } else {
                    return // 为什么是return呢
                }
            }
        }
    }


    override fun measureChild(child: View, widthUsed: Int, heightUsed: Int) {
        super.measureChild(child, widthUsed, heightUsed)
    }

    override fun measureChildWithMargins(child: View, widthUsed: Int, heightUsed: Int) {
        super.measureChildWithMargins(child, widthUsed, heightUsed)
    }

//    override fun getDecoratedMeasuredHeight(child: View): Int {
////        return super.getDecoratedMeasuredHeight(child)
//        val insets: Rect = (child.layoutParams as RecyclerView.LayoutParams).mDecorInsets
//        return child.measuredWidth + insets.top + insets.bottom
//    }
//
//    override fun getDecoratedMeasuredWidth(child: View): Int {
////        return super.getDecoratedMeasuredWidth(child)
//        val insets: Rect = (child.layoutParams as RecyclerView.LayoutParams).mDecorInsets
//        return child.measuredWidth + insets.left + insets.right
//    }

    override fun layoutDecorated(child: View, left: Int, top: Int, right: Int, bottom: Int) {
        super.layoutDecorated(child, left, top, right, bottom)
    }

    override fun layoutDecoratedWithMargins(child: View, left: Int, top: Int, right: Int, bottom: Int) {
        super.layoutDecoratedWithMargins(child, left, top, right, bottom)
    }

    //将指定的View直接回收加至recyclerPool
    override fun removeAndRecycleView(child: View, recycler: RecyclerView.Recycler) {
        super.removeAndRecycleView(child, recycler)
    }

    //将指定位置的View直接回收加至recyclerPool
    override fun removeAndRecycleViewAt(index: Int, recycler: RecyclerView.Recycler) {
        super.removeAndRecycleViewAt(index, recycler)
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return super.isAutoMeasureEnabled()
    }


}

class LayoutAdapter(var data: List<Int>) : RecyclerView.Adapter<LayoutAdapter.LayoutAdapterViewHolder>() {
    class LayoutAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = view.findViewById(R.id.tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_layout, parent, false)
        return LayoutAdapterViewHolder(view)
    }

    override fun getItemCount() = data.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LayoutAdapterViewHolder, position: Int) {
        holder.tv.text = "item ${data[position]}"
    }
}