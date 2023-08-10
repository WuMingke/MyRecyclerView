package com.example.myrecyclerview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration : RecyclerView.ItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = 25f
    }

    // 画在item下面
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val top = view.top
            val left = view.left
            c.drawText("第$i", left.toFloat(), (top + 10).toFloat(), paint) //这里top 增加10 让绘制文字与item重叠
        }
    }

    // 画在item上面
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)


    }

    // 间距
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        parent.adapter?.getItemViewType(position) // TODO: 2023/8/10 mingKE 这里可以获得多类型的类型，从而配合 SpanSizeLookup 设置网格间距

        outRect.top = 30

//        if (position == 0) {
//            outRect.bottom = 10
//        } else {
//            outRect.left = 30
//        }
        if (position % 2 == 0) {
            outRect.left = 30
            outRect.right = 5
        } else {
            outRect.left = 5
            outRect.right = 30
        }
    }

}