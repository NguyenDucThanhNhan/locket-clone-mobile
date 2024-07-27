package com.myproject.locket_clone.viewmodel.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.locket_clone.model.GetCertainFeedsResponse
import com.myproject.locket_clone.model.ReactFeedResponse
import com.myproject.locket_clone.repository.Repository
import kotlinx.coroutines.launch

class FeedViewModel (private val repository: Repository) : ViewModel() {
    private val _getCertainFeedsResponse = MutableLiveData<GetCertainFeedsResponse>()
    val getCertainFeedsResponse: LiveData<GetCertainFeedsResponse> get() = _getCertainFeedsResponse

    fun getCertainFeeds(authorization: String, userId: String, searchId: String, page: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCertainFeeds(authorization, userId, searchId, page)
                _getCertainFeedsResponse.value = response
            } catch (e: Exception) {
                _getCertainFeedsResponse.value = GetCertainFeedsResponse(
                    message = "Error: ${e.message}",
                    status = 400,
                    reasonPhrase = e.message,
                    metadata = null
                )
            }
        }
    }

    fun formatDateString(input: String): String {
        // Tách chuỗi theo dấu '-'
        val datePart = input.split("T")[0] // "2024-07-01"

        // Tách năm, tháng, ngày từ chuỗi
        val parts = datePart.split("-")
        val year = parts[0]
        val month = parts[1]
        val day = parts[2]

        // Định dạng lại thành "ngày/tháng/năm"
        return "$day/$month/$year"
    }

    private val _reactFeedResponse = MutableLiveData<ReactFeedResponse>()
    val reactFeedResponse: LiveData<ReactFeedResponse> get() = _reactFeedResponse

    fun reactFeed(authorization: String, userId: String, feedId: String, icon: String) {
        viewModelScope.launch {
            try {
                val response = repository.reactFeed(authorization, userId, feedId, icon)
                _reactFeedResponse.value = response
            } catch (e: Exception) {
                _reactFeedResponse.value = ReactFeedResponse(
                    message = "Error: ${e.message}",
                    status = 400,
                    reasonPhrase = e.message,
                    metadata = null
                )
            }
        }
    }
}