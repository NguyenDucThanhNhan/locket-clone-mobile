package com.myproject.locket_clone.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.locket_clone.databinding.AllFriendsItemBinding
import com.myproject.locket_clone.model.Friend
import com.squareup.picasso.Picasso

class AllFriendsAdapter(var list: ArrayList<Friend>, val onClickFriend: AllFriendsInterface): RecyclerView.Adapter<AllFriendsAdapter.AllFriendsViewHolder>() {
    inner class AllFriendsViewHolder(val binding: AllFriendsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllFriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = AllFriendsItemBinding.inflate(view, parent, false)
        return AllFriendsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AllFriendsViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.apply {
            txtUserName.text = currentItem.name.firstname
            Picasso.get().load(currentItem.profileImageUrl).into(imgUserAvatar)

            holder.itemView.setOnClickListener {
                onClickFriend.onClickFriend(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}