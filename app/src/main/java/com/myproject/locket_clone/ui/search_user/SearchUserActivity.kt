package com.myproject.locket_clone.ui.search_user

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.locket_clone.databinding.ActivitySearchUserBinding
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.Home
import com.myproject.locket_clone.model.SearchUser
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.recycler_view.SearchUserAdapter
import com.myproject.locket_clone.recycler_view.SearchUserInterface
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.viewmodel.home.HomeViewModel
import com.myproject.locket_clone.viewmodel.home.HomeViewModelFactory

class SearchUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchUserBinding
    private lateinit var adapter: SearchUserAdapter
    private var list: MutableList<SearchUser> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { HomeViewModelFactory(repository) }
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(
            HomeViewModel::class.java)
        list.clear()
        //Tao danh sach
        adapter = SearchUserAdapter(list, object: SearchUserInterface{

            override fun OnClickAddFriend(position: Int) {
                Toast.makeText(this@SearchUserActivity, "OK: ${list[position].id}", Toast.LENGTH_SHORT).show()
            }
        })
        binding.rvSearchUser.adapter = adapter

        binding.rvSearchUser.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false)

        //Nhan du lieu tu HomeActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile
        val friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>?
        val sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?
        val receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?

        binding.btnSearch.setOnClickListener {
            val searchText = binding.edtUserName.text.toString()
            if (userProfile != null) {
                homeViewModel.searchUser(userProfile.signInKey, userProfile.userId, searchText)
            }
        }

        homeViewModel.searchResponse.observe(this, Observer{ response ->
            handleSearchUserResponse(response)
        })



    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleSearchUserResponse(response: Home.SearchResponse) {
        when {
            response.status == 200 -> {
                val metadata = response.metadata ?: return
                list.clear() // Xóa dữ liệu cũ trong list trước khi thêm mới
                metadata.forEach { user ->
                    list.add(SearchUser(user._id, user.fullname, user.profileImageUrl))
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}