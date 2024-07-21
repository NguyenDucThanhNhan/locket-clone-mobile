package com.myproject.locket_clone.recycler_view

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.locket_clone.databinding.VisibilityFriendItemBinding
import com.myproject.locket_clone.model.Visibility
import com.squareup.picasso.Picasso

class VisibilityFriendsAdapter(var list: ArrayList<Visibility>, val onAddVisibility: VisibilityFriendsInterface): RecyclerView.Adapter<VisibilityFriendsAdapter.VisibilityFriendsViewHolder>() {
    inner class VisibilityFriendsViewHolder(val binding: VisibilityFriendItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisibilityFriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = VisibilityFriendItemBinding.inflate(view, parent, false)
        return VisibilityFriendsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: VisibilityFriendsViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.apply {
            txtUserName.text = currentItem.name.firstname + " " + currentItem.name.lastname
            Picasso.get().load(currentItem.profileImageUrl).into(imgUserAvatar)


            // Doi mau khung vien
            if (currentItem.isClick) {
                imgUserAvatar.borderColor = Color.parseColor("#E3A400")
            } else {
                imgUserAvatar.borderColor = Color.parseColor("#595959")
            }
            //Lang nghe item click chon
            holder.itemView.setOnClickListener {
                onAddVisibility.onClickVisibility(position)
                //Doi mau khung vien
//                if (!list[position].isClick) {
//                    imgUserAvatar.borderColor = Color.parseColor("#E3A400")
//                    list[position].isClick = true
//                } else {
//                    imgUserAvatar.borderColor = Color.parseColor("#595959")
//                    list[position].isClick = false
//                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateBorderColor(position: Int) {
        list[position].isClick = !list[position].isClick
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetAllItems() {
        for (item in list) {
            item.isClick = false
        }
        notifyDataSetChanged()
    }
}