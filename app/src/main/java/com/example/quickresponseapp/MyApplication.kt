package com.example.quickresponseapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// For maintaining global app state
@HiltAndroidApp
class MyApplication : Application()