package com.example.quickresponseapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import collectWhileCreated
import com.example.quickresponseapp.profile.ProfileViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // ViewModel to access user profile
    private val profileViewModel: ProfileViewModel by viewModels()
    // Navigation controller and drawer layout
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    // Apply saved language
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppPreferences.applySavedLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Set up navigation controller using navhost
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Navigate to disclaimer screen if hasn't been seen yet
        AppPreferences.hasSeenDisclaimer(this).collectWhileCreated(this) { hasSeen ->
            if (!hasSeen && savedInstanceState == null) {
                navController.navigate(R.id.disclaimerActivity)
            }
        }
        // Set up drawer layout and navigation view
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // Handle item clicks in navigation drawer
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeScreenFragment -> navController.navigate(R.id.homeScreenFragment)
                R.id.editProfileFragment -> navController.navigate(R.id.editProfileFragment)
                R.id.contactsFragment -> navController.navigate(R.id.contactsFragment)
                R.id.messagesFragment -> navController.navigate(R.id.messagesFragment)
                R.id.settingsFragment -> navController.navigate(R.id.settingsFragment)
                R.id.aboutKauriFragment -> navController.navigate(R.id.aboutKauriFragment)
            }
            // Close after selection
            drawerLayout.closeDrawer(GravityCompat.END)
            true
        }
        // Access header views in navigation drawer
        val headerView = navigationView.getHeaderView(0)
        val profileImageView = headerView.findViewById<CircleImageView>(R.id.profileImage)
        val profileNameView = headerView.findViewById<TextView>(R.id.profileName)

        // Load and display profile info in navigation drawer
        profileViewModel.getProfile { profile ->
            profile?.let {
                // Profile name
                profileNameView.text = "Kia Ora, ${it.name}!"
                // Load profile image or display placeholder
                if (!it.profileImagePath.isNullOrEmpty() && it.profileImagePath != "default") {
                    try {
                        val uri = Uri.parse(it.profileImagePath)
                        profileImageView.setImageURI(uri)
                        if (profileImageView.drawable == null) {
                            profileImageView.setImageResource(R.drawable.kaurihead)
                        }
                    } catch (e: Exception) {
                        profileImageView.setImageResource(R.drawable.kaurihead)
                    }
                } else {
                    profileImageView.setImageResource(R.drawable.kaurihead)
                }
            }
        }

        // Set up global banner and menu button
        val bannerView = findViewById<View>(R.id.global_banner)
        val menuButton = bannerView.findViewById<ImageButton>(R.id.menu_button)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        // Hide the banner on disclaimer and registration (can bypass making profile or not accepting disclaimer otherwise)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bannerView.visibility = when (destination.id) {
                R.id.disclaimerActivity -> View.GONE
                R.id.registrationFragment -> View.GONE
                else -> View.VISIBLE
            }
        }
    }
}

