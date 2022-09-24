package com.example.coba.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coba.room.Wisata

@Database(
    entities = [User::class, Wisata::class],
    version = 1
)
abstract class UserDB: RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun WisataDAO() : WisataDAO

    companion object {
        @Volatile private var instance : UserDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserDB::class.java,
                "user.db"
            ).allowMainThreadQueries().build()
    }
}