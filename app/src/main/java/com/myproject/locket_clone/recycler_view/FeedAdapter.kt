package com.myproject.locket_clone.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.FeedItemBinding
import com.myproject.locket_clone.model.Feed
import com.squareup.picasso.Picasso

class FeedAdapter(var list: ArrayList<Feed>, val onClickFeed: FeedInterface): RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    private lateinit var binding: FeedItemBinding
    private lateinit var recyclerView: RecyclerView
    inner class FeedViewHolder(val binding: FeedItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context)
        binding = FeedItemBinding.inflate(view, parent, false)
        return FeedViewHolder(binding)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        //Khoi tao animation
        //Lay context
        val context = holder.binding.root.context
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)

        val currentItem = list[position]
        holder.binding.apply {
            edtDescription.setText(currentItem.description)
            if (currentItem.name.isEmpty()) {
                txtName.text = "You"
                btnMore.setImageResource(R.drawable.ic_more)
            } else {
                txtName.text = currentItem.name
                btnMore.setImageResource(R.drawable.ic_download)
            }
            txtDate.text = currentItem.createdAt
            Picasso.get().load(currentItem.imageUrl).into(imgFeed)

            btnHeart.setOnClickListener {
                onClickFeed.onClickHeart(position)
                it.startAnimation(scaleUp)
            }

            btnHaha.setOnClickListener {
                onClickFeed.onClickHaha(position)
                it.startAnimation(scaleUp)
            }

            btnLike.setOnClickListener {
                onClickFeed.onClickLike(position)
                it.startAnimation(scaleUp)
            }

            btnSad.setOnClickListener {
                onClickFeed.onClickSad(position)
                it.startAnimation(scaleUp)
            }

            btnWow.setOnClickListener {
                onClickFeed.onClickWow(position)
                it.startAnimation(scaleUp)
            }

            btnAngry.setOnClickListener {
                onClickFeed.onClickAngry(position)
                it.startAnimation(scaleUp)
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

    fun getMoreButton(position: Int): ImageButton? {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as? FeedViewHolder
        return viewHolder?.binding?.btnMore
    }
}