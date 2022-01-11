package com.example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.`interface`.CategoryEventListener
import com.example.musicplayer.databinding.CategoryLayoutBinding
import com.model.Category

class CategoryAdapter(val list: List<Category>, val eventListener: CategoryEventListener) :
    RecyclerView.Adapter<CategoryAdapter.Holder>() {

    class Holder(val view: CategoryLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(category: Category) {
            view.categoryName.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: CategoryLayoutBinding =
            CategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            eventListener.onItemClickListener(list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}