package com.example.coba.room

import androidx.room.*


@Dao
interface WisataDAO {

    @Insert
    fun addWisata(wisata: Wisata)

    @Update
    fun updateWisata(wisata: Wisata)

    @Delete
    fun deleteWisata(wisata: Wisata)

    @Query("SELECT * FROM Wisata")
    fun getWisata() : List<Wisata>

    @Query("SELECT * FROM Wisata WHERE id =:wisata_id")
    fun getWisata(wisata_id: Int) : Wisata?
}