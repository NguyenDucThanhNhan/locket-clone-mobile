package com.myproject.locket_clone.viewmodel.user_profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myproject.locket_clone.model.NameChangeResponse
import com.myproject.locket_clone.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileViewModel(private val repository: Repository) : ViewModel() {
    val nameChangeResponse: MutableLiveData<NameChangeResponse> = MutableLiveData()

    fun changeName(token: String, userId: String, firstname: String, lastname: String) {
        val response = repository.changeName(token, userId, firstname, lastname)
        response.enqueue(object : Callback<NameChangeResponse> {
            override fun onResponse(call: Call<NameChangeResponse>, response: Response<NameChangeResponse>) {
                nameChangeResponse.value = response.body()
            }

            override fun onFailure(call: Call<NameChangeResponse>, t: Throwable) {

            }
        })
    }
}