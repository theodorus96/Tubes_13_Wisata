package com.example.coba.room
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Wisata (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nama: String,
    val lokasi: String
)