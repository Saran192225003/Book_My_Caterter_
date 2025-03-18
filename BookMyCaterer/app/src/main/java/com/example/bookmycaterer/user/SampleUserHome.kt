package com.example.bookmycaterer.user

data class SampleUserHome(
    val _id: String,
    val name: String,
    val Catering_name: String,
    val phone: String,
    val Business_Size: String,
    val City: String,
    val license: String,
    val isApproved: Boolean? = true,
    val isReject: Boolean? = false
)

