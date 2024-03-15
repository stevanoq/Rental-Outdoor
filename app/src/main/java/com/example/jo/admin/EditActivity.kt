package com.example.jo.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.example.jo.R;

class EditActivity : AppCompatActivity() {

    lateinit var back_btn : Button
    lateinit var image : ImageView
    lateinit var name : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
    }
}