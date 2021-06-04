package com.alawiyaa.githubuserapi.ui

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alawiyaa.githubuserapi.data.local.DatabaseContract
import com.alawiyaa.githubuserapi.data.local.helper.MappingHelper
import com.alawiyaa.githubuserapi.data.remote.response.ItemsItem
import com.alawiyaa.githubuserapi.databinding.ActivityListFavoriteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListFavoriteActivity : AppCompatActivity(), View.OnClickListener {
    var adapterUser: UserAdapter? = null
    private var listUser = ArrayList<ItemsItem>()
    private lateinit var binding : ActivityListFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }
        contentResolver.registerContentObserver(DatabaseContract.NoteColumns.CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list =
                savedInstanceState.getParcelableArrayList<ItemsItem>(DetailUser.EXTRA_USER)
            if (list != null) {
                adapterUser?.setData(listUser)
            }
        }

        binding.toolBarFav.setNavigationOnClickListener(this)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
           binding.pbFavorite.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(DatabaseContract.NoteColumns.CONTENT_URI, null, null, null, null)

                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.pbFavorite.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                adapterUser = UserAdapter(notes)
                setRecycler()
            } else {
                adapterUser?.setData(notes)
                showDataEmpty()
            }
        }
    }

    private fun showDataEmpty() {
        binding.imgNotFound.visibility = View.VISIBLE
        binding.tvNotFound.visibility = View.VISIBLE
    }

    private fun setRecycler() {
       binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(this@ListFavoriteActivity)
            setHasFixedSize(true)
            adapter = adapterUser
        }
        adapterUser?.setOnItemClickCallback(object : UserAdapter.UserListener {
            override fun onItemClick(item: ItemsItem) {
                val i = Intent(this@ListFavoriteActivity,DetailUser::class.java)
                i.putExtra(DetailUser.EXTRA_LOCAL,item)
                startActivity(i)

            }

        })
    }

    override fun onClick(v: View?) {
        onBackPressed()
    }
}