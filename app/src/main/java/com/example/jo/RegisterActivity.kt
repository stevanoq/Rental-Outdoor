package com.example.jo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    lateinit var et_username : EditText
    lateinit var et_email : EditText
    lateinit var et_password : EditText
    lateinit var et_confirm_password : EditText
    lateinit var btn_regis : Button
    lateinit var btn_login : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        et_username = findViewById(R.id.regis_username)
        et_email = findViewById(R.id.regis_email)
        et_password = findViewById(R.id.regis_password)
        et_confirm_password = findViewById(R.id.regis_Confirm_password)
        btn_login = findViewById(R.id.txt_login)
        btn_regis = findViewById(R.id.btn_register)

        btn_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_regis.setOnClickListener {
            registration()
        }
    }

    private fun registration() {
        val username = et_username.text.toString()
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        val confirm_password = et_confirm_password.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()){
            return Toast.makeText(this, "Isi datamu dulu anjing", Toast.LENGTH_LONG).show()
        }

        var url = "${LoginData.link}/user/index.php?username=$username&password=$password&email=$email"//
        val queue = Volley.newRequestQueue(this)

        val getRequest = object :  StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                //Toast.makeText(this, response, Toast.LENGTH_SHORT).show()

                val myObj = JSONObject(response)
                val responseCode = myObj["code"].toString().toInt()
                val responseMessage = myObj["message"].toString()
                println("respose: $responseCode")

                if (responseCode == 400){
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()
                }
                else{
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                println("error: ${it.message}")
            }) {

        }
        queue.add(getRequest)
    }
}