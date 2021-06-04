package com.alawiyaa.githubuserapi.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alawiyaa.githubuserapi.R
import com.alawiyaa.githubuserapi.data.remote.ApiService
import com.alawiyaa.githubuserapi.data.remote.NetworkProvider
import com.alawiyaa.githubuserapi.data.remote.response.ItemsItem
import com.alawiyaa.githubuserapi.data.remote.response.ResponseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel:ViewModel() {


    val searchUser = MutableLiveData<ArrayList<ItemsItem>>()
    val detailUser = MutableLiveData<ItemsItem>()
    val followUser = MutableLiveData<ArrayList<ItemsItem>>()
   private val dataSource = NetworkProvider.providesHttpAdapter().create(ApiService::class.java)




    fun searchUser(context: Context,login: String){
        dataSource.findUserByUsername(login).enqueue(object : Callback<ResponseUser>{
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                val result = response.body()?.userResponse
                searchUser.postValue(result)
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                Toast.makeText(context, context.getText(R.string.network_error), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun detailUser(context: Context,login: String){
        dataSource.findUserDetailByUsername(login).enqueue(object : Callback<ItemsItem>{
            override fun onResponse(
                call: Call<ItemsItem>,
                response: Response<ItemsItem>
            ) {
                val result = response.body()
                if (result != null){
                    detailUser.postValue(result)
                }
            }


            override fun onFailure(call: Call<ItemsItem>, t: Throwable) {
                Toast.makeText(context, context.getText(R.string.network_error), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun getFollowers(context: Context, login: String){
        dataSource.getFollower(login).enqueue(object : Callback<ArrayList<ItemsItem>>{
            override fun onResponse(
                call: Call<ArrayList<ItemsItem>>,
                response: Response<ArrayList<ItemsItem>>
            ) {
                val result  = response.body()
                if (result != null){
                    followUser.postValue(result)
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                Toast.makeText(context, context.getText(R.string.network_error), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun getFollowing(context: Context,login: String){
        dataSource.getFollowing(login).enqueue(object :Callback<ArrayList<ItemsItem>>{
            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                Toast.makeText(context, context.getText(R.string.network_error), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<ArrayList<ItemsItem>>,
                response: Response<ArrayList<ItemsItem>>
            ) {
                val result  = response.body()
                if (result != null){
                    followUser.postValue(result)
                }
            }
        })
    }



    fun getSearch() : LiveData<ArrayList<ItemsItem>> = searchUser
    fun getDetail() : LiveData<ItemsItem> = detailUser
    fun getFollow() : LiveData<ArrayList<ItemsItem>> = followUser
}