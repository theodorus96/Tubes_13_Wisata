package com.example.coba.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nama: String,
    val borndate: String,
    val email: String,
    val phoneNum: String,
    val username: String,
    val password: String
)
