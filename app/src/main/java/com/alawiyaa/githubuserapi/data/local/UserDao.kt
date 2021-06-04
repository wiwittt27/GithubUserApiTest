package com.alawiyaa.githubuserapi.data.local

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alawiyaa.githubuserapi.data.local.DatabaseContract.NoteColumns.Companion.LOGIN
import com.alawiyaa.githubuserapi.data.local.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.alawiyaa.githubuserapi.data.remote.response.ItemsItem


@Dao
interface UserDao {

    @Insert()
    fun insert(user: ItemsItem): Long

    @Query("DELETE FROM $TABLE_NAME WHERE id= :id")
    fun delete(id: Long): Int

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $LOGIN ASC")
    fun getAll(): Cursor


    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    fun getById(id: Int): Cursor
}