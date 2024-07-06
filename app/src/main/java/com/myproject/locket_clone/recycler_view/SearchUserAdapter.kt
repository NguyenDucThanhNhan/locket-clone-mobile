package com.myproject.locket_clone.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.locket_clone.databinding.SearchUserItemBinding
import com.myproject.locket_clone.model.SearchUser
import com.squareup.picasso.Picasso

class SearchUserAdapter(var list: List<SearchUser>, val onAddFriend: SearchUserInterface): RecyclerView.Adapter<SearchUserAdapter.SearchViewHolder>() {
    inner class SearchViewHolder(val binding: SearchUserItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = SearchUserItemBinding.inflate(view, parent, false)
        return SearchViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.apply {
            txtUserName.text = currentItem.name.firstname + " " + currentItem.name.lastname
            Picasso.get().load(currentItem.profileImage).into(imgUserAvatar)

            // Lang nghe click nut add
            btnAddFriend.setOnClickListener {
                onAddFriend.OnClickAddFriend(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}