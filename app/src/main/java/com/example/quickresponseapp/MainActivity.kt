package com.example.quickresponseapp

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.todolist.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val menuButton = findViewById<MaterialButton>(R.id.menu_button)

        val drawerWidth = (Resources.getSystem().displayMetrics.widthPixels * 0.75).toInt()
        navView.layoutParams.width = drawerWidth
        navView.requestLayout()

        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }*/
    }
}
