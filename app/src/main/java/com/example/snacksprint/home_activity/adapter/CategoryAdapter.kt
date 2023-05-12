package com.example.snacksprint.home_activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.snacksprint.R
import com.example.snacksprint.home_activity.model.CategoryModel
import com.example.snacksprint.home_activity.model.Drink
import com.example.snacksprint.home_activity.model.TagResponseModelItem

class CategoryAdapter(var categoryModelList: MutableList<CategoryModel>, var context: Context?) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_row,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val categoryItem = categoryModelList[position]
        holder.title.text =  categoryItem.title
        holder.view.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.onItemSelected(categoryItem, position)
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tvCategoryName)
        var view: View = itemView
    }

    override fun getItemCount(): Int {
        return categoryModelList.size
    }

    fun setOnClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemSelected(categoryModel: CategoryModel?, position: Int)
    }
}