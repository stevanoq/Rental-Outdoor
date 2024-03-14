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
import com.example.jo.admin.AddItemActivity
import com.example.jo.admin.AdminActivity
import com.example.jo.user.MainActivity
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    lateinit var edit_username : EditText
    lateinit var edit_password : EditText
    lateinit var btn_regis : TextView
    lateinit var btn_login : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edit_username = findViewById(R.id.login_username)
        edit_password = findViewById(R.id.login_password)
        btn_regis = findViewById(R.id.register)
        btn_login = findViewById(R.id.btn_login)

        btn_login.setOnClickListener {
            login()
        }

        btn_regis.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val username = edit_username.text.toString()
        val password = edit_password.text.toString()

        if (username.isEmpty() || password.isEmpty()){
            return Toast.makeText(this, "Isi datamu dulu anjing", Toast.LENGTH_LONG).show()
        }

        var url = "${LoginData.link}/user/index.php?username=$username&password=$password"//
        val queue = Volley.newRequestQueue(this)

        val getRequest = object :  StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                //Toast.makeText(this, response, Toast.LENGTH_SHORT).show()

                val myObj = JSONObject(response)
                val responseCode = myObj["code"].toString().toInt()
                val responseMessage = myObj["message"].toString()
                val uid = myObj["uid"].toString()
                println("respose: $responseCode")

                if (responseCode == 400){
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()
                }
                else{
                    if (username == "admin"){
                        var intent = Intent(this, AdminActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    else{
                        LoginData.UID = uid
                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

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