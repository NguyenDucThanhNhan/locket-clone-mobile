package com.myproject.locket_clone.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.locket_clone.databinding.FeedItemBinding
import com.myproject.locket_clone.model.Feed
import com.myproject.locket_clone.model.FeedMetadata
import com.squareup.picasso.Picasso

class FeedAdapter(var list: ArrayList<Feed>, val onClickFeed: FeedInterface): RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    inner class FeedViewHolder(val binding: FeedItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = FeedItemBinding.inflate(view, parent, false)
        return FeedViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.apply {
            edtDescription.setText(currentItem.description)
            txtName.text = currentItem.name
            txtDate.text = currentItem.createdAt
            Picasso.get().load(currentItem.imageUrl).into(imgFeed)

            btnHeart.setOnClickListener {
                onClickFeed.onClickHeart(position)
            }

            btnHaha.setOnClickListener {
                onClickFeed.onClickHaha(position)
            }

            btnLike.setOnClickListener {
                onClickFeed.onClickLike(position)
            }

            btnSad.setOnClickListener {
                onClickFeed.onClickSad(position)
            }

            btnWow.setOnClickListener {
                onClickFeed.onClickWow(position)
            }

            btnAngry.setOnClickListener {
                onClickFeed.onClickAngry(position)
            }

            btnUserProfile.setOnClickListener {
                onClickFeed.onClickUserProfile(position)
            }

            btnAllFriends.setOnClickListener {
                onClickFeed.onClickAllFriends(position)
            }

            btnSearchUser.setOnClickListener {
                onClickFeed.onClickSearchUser(position)
            }

            btnGrid.setOnClickListener {
                onClickFeed.onClickGrid(position)
            }

            btnMore.setOnClickListener {
                onClickFeed.onClickMore(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}