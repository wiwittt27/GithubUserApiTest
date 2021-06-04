package com.alawiyaa.githubuserapi.ui.locale

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment:Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetTitle()
    }

    override fun onAttach(context: Context) {
        LocalManager.setLocale(context)?.let { super.onAttach(it) }
    }

    private fun resetTitle() {
        try {
            val info =activity?.packageManager?.getActivityInfo(
                activity?.componentName,
                PackageManager.GET_META_DATA
            )
            if (info?.labelRes != 0) {
                info?.labelRes?.let { activity?.setTitle(it) }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}