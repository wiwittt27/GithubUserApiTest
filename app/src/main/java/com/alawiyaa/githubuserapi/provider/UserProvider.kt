package com.alawiyaa.githubuserapi.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.alawiyaa.githubuserapi.data.local.AppDatabase
import com.alawiyaa.githubuserapi.data.local.DatabaseContract.AUTHORITY
import com.alawiyaa.githubuserapi.data.local.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.alawiyaa.githubuserapi.data.local.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.alawiyaa.githubuserapi.data.local.UserDao
import com.alawiyaa.githubuserapi.data.remote.response.ItemsItem

class UserProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userDao: UserDao
        init {

            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)

            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                USER_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val delete: Int = when (USER_ID) {

            sUriMatcher.match(uri) -> userDao.delete(ContentUris.parseId(uri))
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return delete
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> userDao.insert(ItemsItem.itemsContentValues(values))
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
        }

    override fun onCreate(): Boolean {
        userDao = AppDatabase.getDatabase(context as Context).getUserDao()

        return false    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

        return when (sUriMatcher.match(uri)) {
            USER -> userDao.getAll()
            USER_ID -> uri.lastPathSegment?.toInt()?.let { userDao.getById(it) }
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}
