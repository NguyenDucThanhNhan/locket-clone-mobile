package com.myproject.locket_clone.view.user

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.myproject.locket_clone.databinding.ActivityUserBinding
import com.myproject.locket_clone.model.Friend
import com.myproject.locket_clone.model.UserProfile
import com.myproject.locket_clone.repository.Repository
import com.myproject.locket_clone.view.change_birthday.ChangeBirthdayActivity
import com.myproject.locket_clone.view.change_email.ChangeEmail_VerifyPasswordActivity
import com.myproject.locket_clone.view.change_name.ChangeNameActivity
import com.myproject.locket_clone.view.home.HomeActivity
import com.myproject.locket_clone.view.sign_in.SignInActivity
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModel
import com.myproject.locket_clone.viewmodel.user_profile.UserProfileViewModelFactory
import com.soundcloud.android.crop.Crop
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var userProfile: UserProfile

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 101
        private const val REQUEST_CODE_PICK_IMAGE = 102
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khoi tao ban dau
        val repository by lazy { Repository() }
        val viewModelFactory by lazy { UserProfileViewModelFactory(repository) }
        userProfileViewModel = ViewModelProvider(this, viewModelFactory).get(
            UserProfileViewModel::class.java)

        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                permissions,
                REQUEST_CODE_PERMISSIONS
            )
        }

        //Nhan du lieu tu HomeActivity
        userProfile = (intent.getSerializableExtra("USER_PROFILE") as? UserProfile)!!
        val friendList = intent.getSerializableExtra("FRIEND_LIST") as ArrayList<Friend>?
        val sentInviteList = intent.getSerializableExtra("SENT_INVITE_LIST") as ArrayList<Friend>?
        val receivedInviteList = intent.getSerializableExtra("RECEIVED_INVITE_LIST") as ArrayList<Friend>?

        //Gan du lieu vao layout
        if (userProfile != null) {
            Picasso.get().load(userProfile.profileImageUrl).into(binding.imgUserAvatar)
            binding.txtUserName.text = "${userProfile.lastname} ${userProfile.firstname}"
        }

        //Click back
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            intent.putExtra("FRIEND_LIST", friendList)
            intent.putExtra("SENT_INVITE_LIST", sentInviteList)
            intent.putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            startActivity(intent)
        }

        //Click edit infor
        binding.btnEditInfor.setOnClickListener {
            val intent = Intent(this, ChangeNameActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            intent.putExtra("FRIEND_LIST", friendList)
            intent.putExtra("SENT_INVITE_LIST", sentInviteList)
            intent.putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            startActivity(intent)
        }

        //Click sign out
        binding.btnSignOut.setOnClickListener {
            if (userProfile != null) {
                userProfileViewModel.signOut(userProfile.signInKey, userProfile.userId)
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
        }

        //Click change email
        binding.btnChangeEmailAddress.setOnClickListener {
            val intent = Intent(this, ChangeEmail_VerifyPasswordActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            intent.putExtra("FRIEND_LIST", friendList)
            intent.putExtra("SENT_INVITE_LIST", sentInviteList)
            intent.putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            startActivity(intent)
        }

        //Click change birthday
        binding.btnChangeBirthday.setOnClickListener {
            val intent = Intent(this, ChangeBirthdayActivity::class.java)
            intent.putExtra("USER_PROFILE", userProfile)
            intent.putExtra("FRIEND_LIST", friendList)
            intent.putExtra("SENT_INVITE_LIST", sentInviteList)
            intent.putExtra("RECEIVED_INVITE_LIST", receivedInviteList)
            startActivity(intent)
        }

        //Click change image
        binding.btnAddUserPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
//                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                Crop.of(uri, Uri.fromFile(File(cacheDir, "cropped")))
                    .asSquare()
                    .start(this)
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            val croppedUri = Crop.getOutput(data)
            if (croppedUri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, croppedUri)
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false)
                saveImageToGallery(resizedBitmap)

                // Chuẩn bị tệp ảnh để gửi lên server
                val imageFile = File(cacheDir, "cropped")
                val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

                // Gọi hàm updateProfileImage từ ViewModel
                userProfileViewModel.updateProfileImage(
                    authorization = userProfile.signInKey,
                    userId = userProfile.userId,
                    image = body,
                    onSuccess = { response ->
                        runOnUiThread {
                            Toast.makeText(this, "Update profile image successfully", Toast.LENGTH_SHORT).show()
                            val metadata = response.metadata
                            userProfile.profileImageUrl = metadata.profileImageUrl
                            Picasso.get().load(userProfile.profileImageUrl).into(binding.imgUserAvatar)
                        }
                    },
                    onFailure = { errorMessage ->
                        runOnUiThread {
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }


    private fun saveImageToGallery(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
        }
        val contentResolver = contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            contentResolver.openOutputStream(uri).use { outputStream ->
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to save image to gallery", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}