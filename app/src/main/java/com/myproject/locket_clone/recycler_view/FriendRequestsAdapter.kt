package com.myproject.locket_clone.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.locket_clone.databinding.FriendRequestItemBinding
import com.myproject.locket_clone.model.Friend
import com.squareup.picasso.Picasso

class FriendRequestsAdapter(var list: ArrayList<Friend>, val onAddFriend: FriendRequestsInterface): RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestsViewHolder>() {
    inner class FriendRequestsViewHolder(val binding: FriendRequestItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestsViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = FriendRequestItemBinding.inflate(view, parent, false)
        return FriendRequestsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FriendRequestsViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.apply {
            txtUserName.text = currentItem.name.firstname + " " + currentItem.name.lastname
            Picasso.get().load(currentItem.profileImageUrl).into(imgUserAvatar)

            // Lang nghe click nut add
            btnAcceptFriend.setOnClickListener {
                onAddFriend.OnClickAcceptFriend(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}