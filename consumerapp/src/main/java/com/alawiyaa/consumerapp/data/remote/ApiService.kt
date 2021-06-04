package com.alawiyaa.consumerapp.data.remote



import com.alawiyaa.consumerapp.BuildConfig
import com.alawiyaa.consumerapp.data.remote.response.ItemsItem
import com.alawiyaa.consumerapp.data.remote.response.ResponseUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun findUserByUsername(
        @Query("q") username: String
    ): Call<ResponseUser>



    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun findUserDetailByUsername(
        @Path("username") username: String?
    ): Call<ItemsItem>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getFollower(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>
}