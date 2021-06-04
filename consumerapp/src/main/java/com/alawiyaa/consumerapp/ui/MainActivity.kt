package com.alawiyaa.consumerapp.ui


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alawiyaa.consumerapp.R
import com.alawiyaa.consumerapp.data.remote.response.ItemsItem
import com.alawiyaa.consumerapp.databinding.ActivityMainBinding
import com.alawiyaa.consumerapp.viewmodel.UserViewModel
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), UserAdapter.UserListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private var list = ArrayList<ItemsItem>()
    private lateinit var mainViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        adapter = UserAdapter(list)
        adapter.notifyDataSetChanged()
        binding.rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvUser.adapter = adapter




        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        mainViewModel.getSearch().observe(this,  { state ->
            if (state != null ){
                setLayoutDataFound()
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
                adapter.setData(state)
                adapter.notifyDataSetChanged()
                showLoading(false)
                adapter.setOnItemClickCallback(this)
            }
            else{
                showLoading(false)
                setLayoutDataNotFound()
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.item_search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.user_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                    showLoading(true)
                mainViewModel.searchUser(this@MainActivity,query)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.itemFavorite ->{
                val toFavorite = Intent(this@MainActivity, ListFavoriteActivity::class.java)
                startActivity(toFavorite)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> binding.pbLoading.visibility = View.VISIBLE
            false -> binding.pbLoading.visibility = View.GONE
        }
    }

    private fun setLayoutDataFound() {
        showLoading(false)
        binding.rvUser.visibility = View.VISIBLE
        binding.tvNotFound.visibility = View.INVISIBLE
        binding.imgNotFound.visibility = View.INVISIBLE
    }

    private fun setLayoutDataNotFound() {
        showLoading(false)
        binding.rvUser.visibility = View.GONE
        binding.tvNotFound.visibility = View.VISIBLE
        binding.imgNotFound.visibility = View.VISIBLE
    }

    override fun onItemClick(item: ItemsItem) {
        val toDetail = Intent(this@MainActivity,DetailUser::class.java)
        toDetail.putExtra(DetailUser.EXTRA_USER, item)
        startActivity(toDetail)


    }





}