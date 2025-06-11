package com.example.quickresponseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        if (intent.getBooleanExtra("show_disclaimer", false)) {
            navController.navigate(R.id.disclaimerActivity)
        }

        drawerLayout = findViewById(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeScreenFragment -> {
                    navController.navigate(R.id.homeScreenFragment)
                }
                // Add edit profile
                R.id.contactsFragment -> {
                    navController.navigate(R.id.contactsFragment)
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                }
                R.id.aboutKauriFragment -> {
                    navController.navigate(R.id.aboutKauriFragment)
                }
            }

            drawerLayout.closeDrawer(GravityCompat.END)

            true
        }

        val bannerView = findViewById<View>(R.id.global_banner)
        val menuButton = bannerView.findViewById<ImageButton>(R.id.menu_button)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bannerView.visibility = when (destination.id) {
                R.id.disclaimerActivity, R.id.disclaimerActivity -> View.GONE
                else -> View.VISIBLE
            }
        }
    }
}

