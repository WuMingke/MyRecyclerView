package com.example.myrecyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomDActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_item_decoration)


        val rv = findViewById<RecyclerView>(R.id.rv)
//        rv.layoutManager = LinearLayoutManager(this)
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == 0) {
                    return 2
                }
                return 1
            }
        }
        rv.layoutManager = layoutManager
        rv.addItemDecoration(CustomItemDecoration())
        rv.adapter = AAdapter(DataUtils.getStringData())

    }
}

class AAdapter(private val data: List<String>) : RecyclerView.Adapter<AAdapter.AAdapterViewHolder>() {
    class AAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = view.findViewById(R.id.tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_custom_item_decoration_item, parent, false)
        return AAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AAdapterViewHolder, position: Int) {
        holder.tv.text = data[position]
    }
}