package com.myproject.locket_clone.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.locket_clone.databinding.FeedInGridItemBinding
import com.myproject.locket_clone.model.Feed
import com.squareup.picasso.Picasso

class FeedInGridAdapter(var list: ArrayList<Feed>): RecyclerView.Adapter<FeedInGridAdapter.FeedInGridViewHolder>() {
    inner class FeedInGridViewHolder(val binding: FeedInGridItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedInGridViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = FeedInGridItemBinding.inflate(view, parent, false)
        return FeedInGridViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FeedInGridViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.apply {
            Picasso.get().load(currentItem.imageUrl).into(imgFeed)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}