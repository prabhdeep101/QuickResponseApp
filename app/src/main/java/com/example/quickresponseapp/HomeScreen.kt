package com.example.quickresponseapp

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.todolist.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.appbar.MaterialToolbar

class HomeScreen : AppCompatActivity() {

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val menuButton = findViewById<MaterialButton>(R.id.menu_button)
        // Set drawer width to 75% of screen width
        val drawerWidth = (Resources.getSystem().displayMetrics.widthPixels * 0.75).toInt()
        navView.layoutParams.width = drawerWidth
        navView.requestLayout()
        // Open drawer when custom menu button is tapped
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)

        }

    }
}
