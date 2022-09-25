package com.example.coba.room

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    fun getUser() : List<User>

    @Query("SELECT * FROM User WHERE username =:username AND password =:password")
    fun getUser(username: String, password: String)  : User?

    @Query("SELECT * FROM User WHERE id =:user_id")
    fun getUser(user_id: Int) : User?
}