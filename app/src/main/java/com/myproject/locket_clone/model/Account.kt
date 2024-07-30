package com.myproject.locket_clone.model

data class Account(
    val email: String,
    val password: String
)

data class ChangePasswordResponse(
    val status: Int? = null,
    val message: String,
    val reasonPhrase: String? = null,
    val metadata: ChangePasswordMetadata? = null
)

data class ChangePasswordMetadata(
    val code: Int? = null
)

data class ChangeEmailRequest(
    val email: String
)

data class ChangeEmailResponse(
    val status: Int,
    val message: String,
    val reasonPhrase: String? = null,
    val metadata: ChangeEmailMetadata? = null
)

data class ChangeEmailMetadata(
    val fullname: ChangeEmailFullname? = null,
    val _id: String,
    val email: String,
    val password: String,
    val birthday: String,
    val profileImageUrl: String,
    val friends: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class ChangeEmailFullname(
    val firstname: String,
    val lastname: String
)

data class BirthdayChangeRequest(
    val birthday: String
)

data class BirthdayChangeResponse(
    val status: Int,
    val message: String,
    val reasonPhrase: String? = null,
    val metadata: BirthdayChangeMetadata? = null
)

data class BirthdayChangeMetadata(
    val fullname: BirthdayChangeFullname? = null,
    val _id: String,
    val email: String,
    val password: String,
    val birthday: String,
    val profileImageUrl: String,
    val friends: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class BirthdayChangeFullname(
    val firstname: String,
    val lastname: String
)

data class UpdateProfileImageResponse(
    val message: String,
    val status: Int,
    val reasonPhrase: String,
    val metadata: UpdateProfileImageMetadata
)

data class UpdateProfileImageMetadata(
    val fullname: UpdateProfileImageFullname,
    val _id: String,
    val email: String,
    val password: String,
    val birthday: String,
    val profileImageUrl: String,
    val friends: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class UpdateProfileImageFullname(
    val firstname: String,
    val lastname: String
)

data class DeleteAccountResponse(
    val message: String,
    val status: Int,
    val reasonPhrase: String?,
    val metadata: Any?
)