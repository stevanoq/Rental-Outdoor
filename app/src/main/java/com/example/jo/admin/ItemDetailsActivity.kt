package com.example.jo.admin;

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.jo.LoginData

import com.example.jo.R;
import com.example.jo.adapter.ItemsAdapter
import com.example.jo.models.Items
import org.json.JSONArray
import org.json.JSONObject

class ItemDetailsActivity : AppCompatActivity() {

    lateinit var back_btn : ImageButton
    lateinit var edit_btn : Button
    lateinit var delete_btn : Button
    lateinit var image : ImageView
    lateinit var tv_name : TextView
    lateinit var tv_price : TextView
    lateinit var tv_description : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        back_btn = findViewById(R.id.detail_btn_back)
        edit_btn = findViewById(R.id.detail_edit_btn)
        delete_btn = findViewById(R.id.detail_delete_button)
        image = findViewById(R.id.detail_image)
        tv_name = findViewById(R.id.detail_name)
        tv_price = findViewById(R.id.detail_harga)
        tv_description = findViewById(R.id.detail_description)

        back_btn.setOnClickListener{
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }

        edit_btn.setOnClickListener {
            LoginData.editable = true
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
            finish()
        }

        delete_btn.setOnClickListener{
            deleteItem()
        }

        getData()
    }

    private fun deleteItem() {
        val loading = ProgressDialog(this)
        loading.setMessage("Deleting data...")
        loading.show()

        var url = "${LoginData.link}/item/index.php?state=delete&uid=${LoginData.key}"
        val queue = Volley.newRequestQueue(this)

        val getRequest = object :  StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->

                val myObj = JSONObject(response)
                val responseCode = myObj["code"].toString().toInt()
                val responseMessage = myObj["message"].toString()

                if (responseCode == 400){
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()
                }
                else{
                    loading.dismiss()
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()
                    LoginData.key = ""
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }, Response.ErrorListener {
                Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                println("error: ${it.message}")
            }) {

        }
        queue.add(getRequest)
    }

    private fun getData() {
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()

        var url = "${LoginData.link}/item/index.php?state=get&uid=${LoginData.key}"
        val queue = Volley.newRequestQueue(this)

        val getRequest = object :  StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->

                val myObj = JSONObject(response)
                val responseCode = myObj["code"].toString().toInt()
                val responseMessage = myObj["message"].toString()
                val items = JSONObject(myObj["data"].toString())

//                println("items = ${items["id"]}")

                if (responseCode == 400){
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()
                }
                else{
                    loading.dismiss()
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()

                    Glide.with(this)
                        .load("${LoginData.link}${items["image"].toString().substring(1, items["image"].toString().length)}")
                        .into(image)

                    tv_name.text = items["name"].toString()
                    tv_price.text = "Rp.${items["price"]}"
                    tv_description.text = items["description"].toString()

                }

            }, Response.ErrorListener {
                Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                println("error: ${it.message}")
            }) {

        }
        queue.add(getRequest)
    }
}