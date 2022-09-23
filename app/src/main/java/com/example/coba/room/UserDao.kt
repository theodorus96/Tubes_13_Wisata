package com.example.coba.room

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun addNote(user: User)

    @Update
    fun updateNote(user: User)

    @Delete
    fun deleteNote(user: User)

    @Query("SELECT * FROM user")
    fun getNotes() : List<User>

    @Query("SELECT * FROM User WHERE id =:note_id")
    fun getNote(note_id: Int) : List<User>
}