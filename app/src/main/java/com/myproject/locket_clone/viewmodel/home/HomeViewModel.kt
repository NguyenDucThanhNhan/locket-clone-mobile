package com.myproject.locket_clone.viewmodel.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myproject.locket_clone.model.Home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.locket_clone.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _searchResponse = MutableLiveData<Home.SearchResponse>()
    val searchResponse: LiveData<Home.SearchResponse> = _searchResponse

    fun searchUser(authorization: String, userId: String, searchValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.searchUser(authorization, userId, searchValue)
                response.enqueue(object : Callback<Home.SearchResponse> {
                    override fun onResponse(call: Call<Home.SearchResponse>, response: Response<Home.SearchResponse>) {
                        if (response.isSuccessful) {
                            _searchResponse.postValue(response.body())
                            response.body()?.metadata?.forEach { user ->
                                Log.d("SearchResult", "ID: ${user._id}")
                                Log.d("SearchResult", "Name: ${user.fullname.firstname} ${user.fullname.lastname}")
                                Log.d("SearchResult", "Profile Image URL: ${user.profileImageUrl}")
                            }
                        } else {
                            Log.d("SearchResult", "Error: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Home.SearchResponse>, t: Throwable) {
                        Log.e("SearchResult", "Failure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("SearchResult", "Exception: ${e.message}")
            }
        }
    }

    private val _sendInviteResponse = MutableLiveData<Home.SendInviteResponse>()
    val sendInviteResponse: LiveData<Home.SendInviteResponse> get() = _sendInviteResponse

    fun sendInvite(authorization: String, userId: String, friendId: String) {
        viewModelScope.launch {
            try {
                val response = repository.sendInvite(authorization, userId, friendId)
                _sendInviteResponse.value = response
            } catch (e: Exception) {
                _sendInviteResponse.value = Home.SendInviteResponse(
                    message = "Error: ${e.message}",
                    status = 400,
                    reasonPhrase = e.message,
                    metadata = null
                )
            }
        }
    }

    private val _acceptInviteResponse = MutableLiveData<Home.AcceptInviteResponse>()
    val acceptInviteResponse: LiveData<Home.AcceptInviteResponse> get() = _acceptInviteResponse

    fun acceptInvite(authorization: String, userId: String, friendId: String) {
        viewModelScope.launch {
            try {
                val response = repository.acceptInvite(authorization, userId, friendId)
                _acceptInviteResponse.value = response
            } catch (e: Exception) {
                // Xử lý lỗi
                _acceptInviteResponse.value = Home.AcceptInviteResponse(
                    message = "Error: ${e.message}",
                    status = 400,
                    reasonPhrase = e.message,
                    metadata = null
                )
            }
        }
    }

    private val _removeFriendResponse = MutableLiveData<Home.RemoveFriendResponse>()
    val removeFriendResponse: LiveData<Home.RemoveFriendResponse> get() = _removeFriendResponse

    fun removeFriend(authorization: String, userId: String, friendId: String) {
        viewModelScope.launch {
            try {
                val response = repository.removeFriend(authorization, userId, friendId)
                _removeFriendResponse.value = response
            } catch (e: Exception) {
                // Xử lý lỗi
                _removeFriendResponse.value = Home.RemoveFriendResponse(
                    message = "Error: ${e.message}",
                    status = 400,
                    reasonPhrase = e.message,
                    metadata = null
                )
            }
        }
    }

    private val _userInfoResponse = MutableLiveData<Home.UserInfoResponse>()
    val userInfoResponse: LiveData<Home.UserInfoResponse> get() = _userInfoResponse

    fun getUserInfo(authorization: String, userId: String, targetUserId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserInfo(authorization, userId, targetUserId)
                _userInfoResponse.value = response
            } catch (e: Exception) {
                // Xử lý lỗi
                _userInfoResponse.value = Home.UserInfoResponse(
                    message = "Error: ${e.message}",
                    status = 400,
                    reasonPhrase = e.message,
                    metadata = null
                )
            }
        }
    }
}