package com.example.jo.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.jo.LoginData
import com.example.jo.R

class MainActivity : AppCompatActivity() {

    lateinit var btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.main_button)

        btn.setOnClickListener {
            Toast.makeText(this, "${LoginData.UID}", Toast.LENGTH_LONG).show()
        }

    }
}