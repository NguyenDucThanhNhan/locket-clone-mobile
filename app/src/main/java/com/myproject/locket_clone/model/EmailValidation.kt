package com.myproject.locket_clone.model

data class EmailValidationRequest(
    val email: String
)

data class EmailValidationResponse(
    val status: Int? = null,
    val message: String,
    val reasonPhrase: String? = null,
    val metadata: Metadata? = null
)

data class Metadata(
    val code: Int? = null
)