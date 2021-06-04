package com.alawiyaa.githubuserapi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alawiyaa.githubuserapi.R
import com.alawiyaa.githubuserapi.data.remote.response.ItemsItem
import com.alawiyaa.githubuserapi.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class UserAdapter(private val listUser : ArrayList<ItemsItem>): RecyclerView.Adapter<UserAdapter.AdapterUser>() {

    private var onItemClickCallback: UserListener? = null



    fun setOnItemClickCallback(onItemClickCallback: UserListener) {
        this.onItemClickCallback = onItemClickCallback
    }
    fun setData(items: ArrayList<ItemsItem>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterUser {
        return AdapterUser(LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false))
    }

    override fun onBindViewHolder(holder: AdapterUser, position: Int) {
        val item = listUser[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClick(item)

        }

    }

    override fun getItemCount(): Int = listUser.size

    inner class AdapterUser(itemView:View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemUserBinding.bind(itemView)
        fun bind(user : ItemsItem){
            with(itemView){
                binding.tvUsername.text = user.login
                Glide.with(context)
                    .load(user.avatarUrl)
                    .into(binding.imgUser)
            }
        }
    }

    interface UserListener {
        fun onItemClick(item: ItemsItem)

    }
}