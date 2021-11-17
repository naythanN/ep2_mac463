package com.ln.lnplayer
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle

//package com.ln.lnplayer

import android.app.Fragment
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.ln.lnplayer.databinding.ActivityMainBinding
import com.ln.lnplayer.fragments.*
import android.util.Log
import android.view.View
import com.ln.lnplayer.MainActivity
import com.ln.lnplayer.R

//
//class PageManager : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_page_manager)
//    }
//}
//

class PageManager : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val settingsFragment = SettingsFragment()
    private val weatherFragment = WeatherFragment()
    private val libraryFragment = LibraryFragment()
    private val mapsFragment = MapsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_manager)

        //replaceFragment(settingsFragment)
        replaceFragment(homeFragment)

        var bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        var frame = findViewById<FrameLayout>(R.id.fragment_container)

        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_maps -> replaceFragment(mapsFragment)
                R.id.ic_weather -> replaceFragment(weatherFragment)
                R.id.ic_library -> replaceFragment(libraryFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
            }
            true
        }
    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "A activity foi pausada.")
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment){
        if (fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    companion object {
        private const val TAG = "PageMngr"
    }

}