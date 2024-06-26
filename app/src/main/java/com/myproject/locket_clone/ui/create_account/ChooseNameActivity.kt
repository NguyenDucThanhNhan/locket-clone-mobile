package com.myproject.locket_clone.ui.create_account

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.databinding.ActivityChooseNameBinding
import com.myproject.locket_clone.model.SignupResponse
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.ui.login.LoginActivity
import com.myproject.locket_clone.viewmodel.CreateAccountViewModelFactory
import com.myproject.locket_clone.viewmodel.create_account.CreateAccountViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChooseNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var birthday: String = ""
        val email = intent.getStringExtra("USER_EMAIL")!!
        val password = intent.getStringExtra("USER_PASSWORD")!!

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { CreateAccountViewModelFactory(repository) }
        val createAccountViewModel = ViewModelProvider(this, viewModelFactory).get(
            CreateAccountViewModel::class.java)

        binding.btnBirthday.setOnClickListener {
            showDatePicker(this) { formattedDate ->
                // Lưu trữ formattedDate vào biến birthday hoặc làm gì đó khác với nó
                birthday = formattedDate
            }
        }

        binding.btnContinue.setOnClickListener {
            val firstname = binding.edtFirstName.text.toString()
            val lastname = binding.edtLastName.text.toString()

            createAccountViewModel.signup(email, password, firstname, lastname, birthday)

            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("USER_EMAIL", email)
            intent.putExtra("USER_PASSWORD", password)
            startActivity(intent)
        }

        createAccountViewModel.signupResponse.observe(this, Observer { response ->
            handleSignupResponse(response)
        })
    }

    private fun handleSignupResponse(response: SignupResponse) {
        when {
            response.status == 201 -> {
                val metadata = response.metadata ?: return
                val message = "Created user successfully\n" +
                        "Email: ${metadata.email}\n" +
                        "Fullname: ${metadata.fullname.firstname} ${metadata.fullname.lastname}\n" +
                        "Birthday: ${metadata.birthday}\n" +
                        "Profile Image URL: ${metadata.profileImageUrl}\n" +
                        "User ID: ${metadata._id}\n" +
                        "Created At: ${metadata.createdAt}\n" +
                        "Updated At: ${metadata.updatedAt}"
                Log.d("Profile", message)
            }
            response.status == 403 -> {
                Toast.makeText(this, "Data is invalid", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Error: ${response.message}", Toast.LENGTH_LONG).show()

            }
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
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
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