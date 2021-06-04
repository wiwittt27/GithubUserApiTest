package com.alawiyaa.githubuserapi.ui

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.alawiyaa.githubuserapi.R
import com.alawiyaa.githubuserapi.data.local.DatabaseContract
import com.alawiyaa.githubuserapi.data.local.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.alawiyaa.githubuserapi.data.local.helper.MappingHelper
import com.alawiyaa.githubuserapi.data.remote.response.ItemsItem
import com.alawiyaa.githubuserapi.databinding.ActivityDetailUserBinding
import com.alawiyaa.githubuserapi.ui.fragment.SectionPagerAdapter
import com.alawiyaa.githubuserapi.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_LOCAL = "extra_local"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

    private lateinit var uriWithId: Uri
    private lateinit var mainModel: UserViewModel
    private var isEdit = false
    private lateinit var binding: ActivityDetailUserBinding
    private  var items: ItemsItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager =  binding.contentDetailApp.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.contentDetailApp.tabs
        TabLayoutMediator(tabs,viewPager ){tab, position->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        setupToolbar()
        mainModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        if (intent.getParcelableExtra<ItemsItem>(EXTRA_USER) != null){
            items = intent.getParcelableExtra(EXTRA_USER)
            items?.login?.let { mainModel.detailUser(this, it) }
        }else if(intent.getParcelableExtra<ItemsItem>(EXTRA_LOCAL) != null){
            items = intent.getParcelableExtra(EXTRA_LOCAL)
            getDataLocal(items)
        }




        mainModel.getDetail().observe(this ,{state->
            if (state != null){
                showLoading(false)
                binding.contentDetailApp.tvDetailName.text  = state.name
                binding.contentDetailApp.tvDetailLocation.text = state.location
                binding.contentDetailApp.tvDetailCompany.text = state.company
                binding.contentDetailApp.tvDetailRepository.text = state.publicRepos
                Glide.with(this@DetailUser).load(state.avatarUrl).into(binding.imgPosterDetail)

            }
        })
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = items?.login
        binding.toolbar.setNavigationOnClickListener{
            onBackPressed()
        }

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + items?.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            isEdit = true
            items = MappingHelper.mapCursorToObject(cursor)
            cursor.close()
        }

        setStatus(isEdit)
        binding.fabFavorite.setOnClickListener {
            if (isEdit) {
                isEdit = !isEdit
                contentResolver.delete(uriWithId, null, null)
                snackBarShow(getString(R.string.succes_delete), getString(R.string.title_seeData))
                setStatus(isEdit)
            } else {
                isEdit = !isEdit
                val name = binding.contentDetailApp.tvDetailName.text.toString()
                val company = binding.contentDetailApp.tvDetailCompany.text.toString()
                val location = binding.contentDetailApp.tvDetailLocation.text.toString()
                val repo = binding.contentDetailApp.tvDetailRepository.text.toString()

                val values = ContentValues()
                values.put(DatabaseContract.NoteColumns._ID, items?.id)
                values.put(DatabaseContract.NoteColumns.LOGIN, items?.login)
                values.put(DatabaseContract.NoteColumns.NAME, name)
                values.put(DatabaseContract.NoteColumns.COMPANY, company)
                values.put(DatabaseContract.NoteColumns.LOCATION, location)
                values.put(DatabaseContract.NoteColumns.REPOSITORY, repo)
                values.put(DatabaseContract.NoteColumns.FOLLOWERS, items?.followersUrl)
                values.put(DatabaseContract.NoteColumns.FOLLOWING, items?.followingUrl)
                values.put(DatabaseContract.NoteColumns.IMAGE, items?.avatarUrl)

                contentResolver.insert(CONTENT_URI, values)
                snackBarShow(getString(R.string.succes_add), getString(R.string.title_seeData))
                setStatus(isEdit)
            }
        }
        }

    private fun snackBarShow(value: String, info: String) {
        Snackbar.make(binding.contentDetailApp.container, value, Snackbar.LENGTH_SHORT).setAction(info) {
            val showFav = Intent(this, ListFavoriteActivity::class.java)
            startActivity(showFav)
        }.show()
    }



    private fun setStatus(edit: Boolean) {
        if (edit) {
           binding.fabFavorite.setImageResource(R.drawable.ic_star_enable)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_star_disable)
        }
    }

    private fun getDataLocal(items: ItemsItem?) {
        showLoading(false)
       binding.contentDetailApp.tvDetailName.text = items?.name ?: "null"
        binding.contentDetailApp.tvDetailLocation.text = items?.location ?: "null"
        binding.contentDetailApp.tvDetailCompany.text = items?.company ?: "null"
        binding.contentDetailApp.tvDetailRepository.text = items?.publicRepos ?: "null"
        Glide.with(this@DetailUser).load(items?.avatarUrl).into(binding.imgPosterDetail)

    }

    private fun setupToolbar() {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
       binding.toolbarLayout.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                this,
                R.color.colorWhite
            )
        )
        binding.toolbarLayout.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                R.color.transparent
            )
        )

    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> binding.pbDetail.visibility = View.VISIBLE
            false -> binding.pbDetail.visibility = View.GONE
        }

    }
}