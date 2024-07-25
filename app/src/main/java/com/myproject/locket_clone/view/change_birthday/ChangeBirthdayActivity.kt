package com.myproject.locket_clone.view.change_birthday

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.R
import com.myproject.locket_clone.databinding.ActivityChangeBirthdayBinding
import com.myproject.locket_clone.model.BirthdayChangeResponse
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.view.user.UserActivity
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModel
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChangeBirthdayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeBirthdayBinding
    private lateinit var friendList: ArrayList<Friend>
    private lateinit var sentInviteList: ArrayList<Friend>
    private lateinit var receivedInviteList: ArrayList<Friend>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeBirthdayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { UserProfileViewModelFactory(repository) }
        val userProfileViewModel = ViewModelProvider(this, viewModelFactory).get(
            UserProfileViewModel::class.java)
        var newBirthday = ""
        //Nhan du lieu tu UserActivity
        val userProfile: UserProfile? = intent.getSerializableExtra("USER_PROFILE") as? UserProfile
        friendList = (intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>?)!!
        sentInviteList = (intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?)!!
        receivedInviteList = (intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?)!!
        if (userProfile != null) {
            binding.txtBirthday.text = userProfile.birthday
        }
        binding.btnSave.isEnabled = false
        binding.btnSave.setBackgroundResource(R.drawable.btn_gray_button)


        binding.btnBirthday.setOnClickListener {
            showDatePicker(this) { formattedDate ->
                // Lưu trữ formattedDate vào biến birthday hoặc làm gì đó khác với nó
                newBirthday = formattedDate
                binding.txtBirthday.text = newBirthday
                binding.btnSave.isEnabled = true
                binding.btnSave.setBackgroundResource(R.drawable.btn_yellow_button)
            }
        }

        binding.btnSave.setOnClickListener {
            if (userProfile != null) {
                userProfileViewModel.changeBirthday(
                    userProfile.signInKey,
                    userProfile.userId,
                    newBirthday,
                    onSuccess = { response ->
                        handleBirthdayChangeResponse(response, userProfile, newBirthday)
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    }
                )
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            intent.putExtra("FRIEND_LIST", friendList)
            intent.putExtra("SENT_INVITE_LIST", sentInviteList)
            intent.putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            startActivity(intent)
        }
    }

    private fun handleBirthdayChangeResponse(response: BirthdayChangeResponse, userProfile: UserProfile, newBirthday: String) {
        if (response.status == 200) {
            val user = userProfile.copy(birthday = newBirthday)
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("USER_PROFILE", user)
            intent.putExtra("FRIEND_LIST", friendList)
            intent.putExtra("SENT_INVITE_LIST", sentInviteList)
            intent.putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            startActivity(intent)
        } else {
            Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
        }
    }

    // Hàm showDatePicker trả về chuỗi "dd/mm/yyyy" khi người dùng chọn ngày
    private fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Định dạng ngày thành chuỗi "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Gọi hàm callback và trả về chuỗi định dạng
                onDateSelected.invoke(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }
}