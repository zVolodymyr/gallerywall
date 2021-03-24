package com.baysoft.gallerywall


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mi_refresh) {
            val intent = Intent(context, GalleryWallService::class.java)
            intent.action = GalleryWallService.UPDATE_ACTION;
            activity?.startService(intent);
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<ListPreference>(Settings.PREF_PERIOD)?.run {
            setOnPreferenceChangeListener { pref, newValue ->

                newValue.toString().toLongOrNull()?.run {
                    when (this) {
                        0L -> GalleryWall.cancelSchedule(requireActivity())
                        else -> {
                            GalleryWall.schedule(requireActivity(), this)
                        }
                    }
                }

                true
            }
        }

    }

    private fun clearPrefs() {
        preferenceManager.sharedPreferences.edit().clear().apply()
    }
}