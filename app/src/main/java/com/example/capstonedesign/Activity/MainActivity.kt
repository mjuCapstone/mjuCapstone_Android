package com.example.capstonedesign.Activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.capstonedesign.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //상단 툴바 설정
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.toolbar)
        var btnBack : ImageButton? = supportActionBar?.customView?.findViewById(R.id.btnBack)
        btnBack?.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }
        //하단 네비게이션 바 설정
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
    }
}