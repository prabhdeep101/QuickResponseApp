package com.example.quickresponseapp

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.todolist.R
import com.example.todolist.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bannerView = findViewById<View>(R.id.global_banner)
        val menuButton = bannerView.findViewById<ImageButton>(R.id.menu_button)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }
}

