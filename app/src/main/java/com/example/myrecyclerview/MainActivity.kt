package com.example.myrecyclerview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val mDataAdapter = DataAdapter(DataUtils.getData())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.adapter = mDataAdapter

    }

    fun changeData(view: View) {
        val oldData = mDataAdapter.data
        val newData = DataUtils.getNewData()
        val result: DiffResult = DiffUtil.calculateDiff(MyCallback(oldData, newData))
        mDataAdapter.data = newData
        result.dispatchUpdatesTo(mDataAdapter)

    }
}

class MyCallback(
    private val oldData: List<Int>,
    private val newData: List<Int>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }

}

class DataAdapter(var data: List<Int>) : RecyclerView.Adapter<DataAdapter.DataAdapterViewHolder>() {
    class DataAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = view.findViewById(R.id.tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_data, parent, false)
        val holder = DataAdapterViewHolder(view)
        Log.i(DataUtils.TAG, "onCreateViewHolder: ${holder.layoutPosition}")
        return holder
    }

    override fun getItemCount() = data.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.tv.text = "item ${data[position]}"
    }
}