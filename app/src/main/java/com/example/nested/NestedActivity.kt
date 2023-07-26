package com.example.nested

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myrecyclerview.R

class NestedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WebView 和 RecyclerView 连贯滑动
        setContentView(R.layout.activity_nested)
    }
}