package com.myproject.locket_clone.ui.search_user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myproject.locket_clone.databinding.ActivitySearchUserBinding
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.Fullname
import com.myproject.locket_clone.model.Home
import com.myproject.locket_clone.model.SearchUser
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.recycler_view.SearchUserAdapter
import com.myproject.locket_clone.recycler_view.SearchUserInterface
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.ui.home.HomeActivity
import com.myproject.locket_clone.viewmodel.home.HomeViewModel
import com.myproject.locket_clone.viewmodel.home.HomeViewModelFactory

class SearchUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchUserBinding
    private lateinit var adapter: SearchUserAdapter
    private var list: MutableList<SearchUser> = mutableListOf()
    private var sentInviteList: ArrayList<Friend> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { HomeViewModelFactory(repository) }
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(
            HomeViewModel::class.java)

        //Nhan du lieu tu HomeActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile
        val friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>?
        sentInviteList = (intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?)!!
        val receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?

        list.clear()
        adapter = SearchUserAdapter(list, object: SearchUserInterface{

            override fun OnClickAddFriend(position: Int) {
                if (userProfile != null) {
                    homeViewModel.sendInvite(userProfile.signInKey, userProfile.userId, list[position].id)
                }
            }
        })
        binding.rvSearchUser.adapter = adapter

        binding.rvSearchUser.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_PROFILE", userProfile)
                putExtra("FRIEND_LIST", friendList)
                putExtra("SENT_INVITE_LIST", sentInviteList)
                putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            }
            startActivity(intent)
        }

        binding.btnSearch.setOnClickListener {
            val searchText = binding.edtUserName.text.toString()
            if (userProfile != null && searchText.isNotEmpty()) {
                homeViewModel.searchUser(userProfile.signInKey, userProfile.userId, searchText)
            }
        }

        homeViewModel.searchResponse.observe(this) { response ->
            handleSearchUserResponse(response)
        }

        homeViewModel.sendInviteResponse.observe(this) { response ->
            handleSendInviteResponse(response)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleSearchUserResponse(response: Home.SearchResponse) {
        when {
            response.status == 200 -> {
                val metadata = response.metadata ?: return
                list.clear()
                metadata.forEach { user ->
                    list.add(SearchUser(user._id, user.fullname, user.profileImageUrl))
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun handleSendInviteResponse(response: Home.SendInviteResponse) {
        when (response.status) {
            200 -> {
                val metadata = response.metadata ?: return
                for (friendData in metadata.sentInviteList) {
                    val id = friendData.id
                    val firstname = friendData.name.firstname
                    val lastname = friendData.name.lastname
                    val profileImageUrl = friendData.profileImageUrl

                    val friend = Friend(id, Fullname(firstname, lastname), profileImageUrl)
                    sentInviteList.add(friend)
                }

                for (friendData in sentInviteList) {
                    Log.d("DEBUG", friendData.name.firstname + " " + friendData.name.lastname)
                }

            }

            400 -> {
                val responseText = when (response.message) {
                    "Friend id is required" -> "Friend id is required"
                    "Friend is not existing" -> "Friend is not existing"
                    "They are already friends" -> "They are already friends"
                    "Friend sent invite before" -> "Friend sent invite before"
                    "User sent invite before" -> "User sent invite before"
                    else -> "You have sent or received an invitation or are already friends with this user"
                }
                Toast.makeText(this, responseText, Toast.LENGTH_LONG).show()
            }
            else -> {
                val responseText = response.message
                Toast.makeText(this, responseText, Toast.LENGTH_LONG).show()
            }
        }
    }
}