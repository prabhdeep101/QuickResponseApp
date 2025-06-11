package com.example.quickresponseapp.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quickresponseapp.R

class MessagesTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_fragment)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MessagesFragment())
            .commit()
    }
}