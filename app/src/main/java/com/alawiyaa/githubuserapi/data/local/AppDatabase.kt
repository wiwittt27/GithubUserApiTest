package com.alawiyaa.githubuserapi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alawiyaa.githubuserapi.data.remote.response.ItemsItem



@Database(
    entities = [ItemsItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase:RoomDatabase() {
    companion object {

        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    // Create database here
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "db_githubapi"
                    )
                        .allowMainThreadQueries() //allows Room to executing task in main thread
                        .fallbackToDestructiveMigration() //allows Room to recreate database if no migrations found
                        .build()

                    INSTANCE = instance
                    instance
                }
        }
    }

    abstract fun getUserDao(): UserDao
}