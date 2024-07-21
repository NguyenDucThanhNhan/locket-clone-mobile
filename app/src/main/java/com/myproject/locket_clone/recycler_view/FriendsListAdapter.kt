package com.myproject.locket_clone.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.locket_clone.databinding.FriendListItemBinding
import com.myproject.locket_clone.model.Friend
import com.squareup.picasso.Picasso

class FriendsListAdapter(var list: ArrayList<Friend>, val onRemoveFriend: FriendsListInterface): RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder>() {
    inner class FriendsListViewHolder(val binding: FriendListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = FriendListItemBinding.inflate(view, parent, false)
        return FriendsListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FriendsListViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.apply {
            txtUserName.text = currentItem.name.firstname + " " + currentItem.name.lastname
            Picasso.get().load(currentItem.profileImageUrl).into(imgUserAvatar)

            // Lang nghe click nut remove
            btnRemoveFriend.setOnClickListener {
                onRemoveFriend.onClickRemoveFriend(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}