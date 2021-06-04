package com.alawiyaa.githubuserapi.ui.reminder

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.alawiyaa.githubuserapi.R
import com.alawiyaa.githubuserapi.databinding.SettingsActivityBinding
import com.alawiyaa.githubuserapi.ui.MainActivity
import com.alawiyaa.githubuserapi.ui.locale.BaseActivity
import com.alawiyaa.githubuserapi.ui.locale.LocalManager

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding : SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarSetting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarSetting.setNavigationOnClickListener(this)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        companion object {
            private const val DEFAULT_VALUE = false
        }


        private lateinit var alarmReceiver: AlarmReceiver
        private lateinit var enablePrefence: SwitchPreference
        private lateinit var selectLanguage: ListPreference
        private lateinit var ENABLE_STATE: String
        private lateinit var LIST_LANGUAGE: String
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            alarmReceiver = AlarmReceiver()
            ENABLE_STATE = resources.getString(R.string.key_switch)
            LIST_LANGUAGE = resources.getString(R.string.key_languaage)
            enablePrefence = findPreference<SwitchPreference>(ENABLE_STATE) as SwitchPreference
            selectLanguage = findPreference<ListPreference>(LIST_LANGUAGE) as ListPreference


            selectLanguage.setOnPreferenceChangeListener { _, newValue ->

                val text = newValue.toString()
                val index = selectLanguage.findIndexOfValue(text)

                if (index == 0) {

                    setNewLocale(LocalManager.INDONESIA)

                } else {

                    setNewLocale(LocalManager.ENGLISH)


                }
                true
            }


            enablePrefence.setOnPreferenceChangeListener{_,_->
                if (enablePrefence.isChecked) {
                    Toast.makeText(context, getString(R.string.reminder_off), Toast.LENGTH_SHORT).show()

                    alarmReceiver.cancelAlarm(context, AlarmReceiver.TYPE_REPEATING)
                    enablePrefence.isChecked = false
                } else {
                    val repeatTime = "09:00"
                    val repeatMessage = getString(R.string.title_back_application)
                    context?.let {
                        alarmReceiver.setRepeatingAlarm(
                            it, AlarmReceiver.TYPE_REPEATING,
                            repeatTime, repeatMessage
                        )
                    }
                    Toast.makeText(
                        context,
                        getString(R.string.title_set_reminder)+" "+ repeatTime + " AM",
                        Toast.LENGTH_SHORT
                    ).show()

                    enablePrefence.isChecked = true
                }
                false
            }
        }
        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        private fun setNewLocale(
            @LocalManager.LocaleDef language: String
        ) {
            context?.let { LocalManager.setNewLocale(it, language) }

            val res = Intent(activity, MainActivity::class.java)
            res.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(res)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (key == ENABLE_STATE) {
                enablePrefence.switchTextOff = sharedPreferences?.getBoolean(
                    ENABLE_STATE,
                    DEFAULT_VALUE
                ).toString()
            }
        }
    }

    override fun onClick(v: View?) {
     onBackPressed()
    }
}