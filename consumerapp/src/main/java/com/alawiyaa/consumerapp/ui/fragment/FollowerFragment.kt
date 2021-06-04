package com.alawiyaa.consumerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alawiyaa.consumerapp.data.remote.response.ItemsItem
import com.alawiyaa.consumerapp.databinding.FragmentFollowerBinding
import com.alawiyaa.consumerapp.ui.DetailUser
import com.alawiyaa.consumerapp.ui.UserAdapter
import com.alawiyaa.consumerapp.viewmodel.UserViewModel


class FollowerFragment : Fragment() {
    private var items: ItemsItem? = null


    private lateinit var mainModel: UserViewModel
    private var _binding : FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity?.intent?.getParcelableExtra<ItemsItem>(DetailUser.EXTRA_USER) != null){
            items = activity?.intent?.getParcelableExtra(DetailUser.EXTRA_USER)

        }else if(activity?.intent?.getParcelableExtra<ItemsItem>(DetailUser.EXTRA_LOCAL) != null){
            items = activity?.intent?.getParcelableExtra(DetailUser.EXTRA_LOCAL)

        }
        mainModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        activity?.let {
            mainModel.getFollow().observe(it,{ state->
                if (state != null){
                    setLayoutDataFound()
                    binding.rvFollowers.adapter = UserAdapter(state)
                }else{
                    setLayoutDataNotFound()
                }
            })
        }

        activity?.let { items?.login?.let { it1 -> mainModel.getFollowers(it, it1) } }
        setLayoutManager()

    }

    private fun setLayoutManager() {
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setLayoutDataFound() {
        showLoading(false)
       binding.rvFollowers.visibility = View.VISIBLE
        binding.tvFollowers.visibility = View.INVISIBLE
        binding.imgFollowers.visibility = View.INVISIBLE
    }

    private fun setLayoutDataNotFound() {
        showLoading(false)
        binding.rvFollowers.visibility = View.GONE
        binding.tvFollowers.visibility = View.VISIBLE
        binding.imgFollowers.visibility = View.VISIBLE
    }
    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> binding.pbFollowers.visibility = View.VISIBLE
            false -> binding.pbFollowers.visibility = View.GONE
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}