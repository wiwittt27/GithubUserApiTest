package com.alawiyaa.consumerapp.data.remote.response

import android.content.ContentValues
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion.COMPANY
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion.FOLLOWERS
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion.FOLLOWING
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion.IMAGE
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion.LOCATION
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion.LOGIN
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion.NAME
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion.REPOSITORY
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.alawiyaa.consumerapp.data.local.DatabaseContract.NoteColumns.Companion._ID
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ResponseUser(

    @SerializedName("total_count")
    val totalCount: Int = 0,


    @SerializedName("items")
    val userResponse: ArrayList<ItemsItem>
) : Parcelable
@Entity(tableName = TABLE_NAME)
@Parcelize
data class ItemsItem(

    @ColumnInfo(name = _ID)
    @SerializedName(_ID)
    @PrimaryKey
    var id: Int = 0,

    @ColumnInfo(name = LOGIN)
    @SerializedName(LOGIN)
    var login: String? = null,

    @ColumnInfo(name = NAME)
    @SerializedName(NAME)
    var name: String? = null,

    @ColumnInfo(name = COMPANY)
    @SerializedName(COMPANY)
    var company: String? = null,


    @ColumnInfo(name = LOCATION)
    @SerializedName(LOCATION)
    var location: String? = null,

    @ColumnInfo(name = REPOSITORY)
    @SerializedName(REPOSITORY)
    var publicRepos: String? = null,

    @ColumnInfo(name = FOLLOWING)
    @SerializedName(FOLLOWING)
    var followingUrl: String? = null,

    @ColumnInfo(name = FOLLOWERS)
    @SerializedName(FOLLOWERS)
    var followersUrl: String? = null,

    @ColumnInfo(name = IMAGE)
    @SerializedName(IMAGE)
    var avatarUrl: String? = null
) : Parcelable{
    companion object{
        fun itemsContentValues(contentValues: ContentValues?): ItemsItem {
            val person =
                ItemsItem()
            if (contentValues != null) {
                if (contentValues.containsKey(_ID)) {
                    person.id = contentValues.getAsInteger(_ID)
                }
            }
            if (contentValues != null) {
                if (contentValues.containsKey(LOGIN)) {
                    person.login = contentValues.getAsString(LOGIN)
                }
            }
            if (contentValues != null) {
                if (contentValues.containsKey(NAME)) {
                    person.name = contentValues.getAsString(NAME)
                }
            }
            if (contentValues != null) {
                if (contentValues.containsKey(COMPANY)) {
                    person.company = contentValues.getAsString(COMPANY)
                }
            }
            if (contentValues != null) {
                if (contentValues.containsKey(LOCATION)) {
                    person.location = contentValues.getAsString(LOCATION)
                }
            }
            if (contentValues != null) {
                if (contentValues.containsKey(REPOSITORY)) {
                    person.publicRepos = contentValues.getAsString(REPOSITORY)
                }
            }
            if (contentValues != null) {
                if (contentValues.containsKey(FOLLOWING)) {
                    person.followingUrl = contentValues.getAsString(FOLLOWING)
                }
            }
            if (contentValues != null) {
                if (contentValues.containsKey(FOLLOWERS)) {
                    person.followersUrl = contentValues.getAsString(FOLLOWERS)
                }
            }
            if (contentValues != null) {
                if (contentValues.containsKey(IMAGE)) {
                    person.avatarUrl = contentValues.getAsString(IMAGE)
                }
            }
            return person
        }
    }
}

