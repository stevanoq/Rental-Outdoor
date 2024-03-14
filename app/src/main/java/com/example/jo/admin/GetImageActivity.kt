package com.example.jo.admin

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.jo.R
import com.example.jo.adapter.ItemsAdapter
import com.example.jo.models.Items
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL


class GetImageActivity : AppCompatActivity() {

    lateinit var btn : Button
    lateinit var imageView: ImageView
    lateinit var image : Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_image)

        btn = findViewById(R.id.buttonGetImage)
        imageView = findViewById(R.id.imageViewShow)

        btn.setOnClickListener {
            getImage()
        }

    }

    private fun getImage() {

        var url = "${R.string.localhost}/item/upload.php"
        val queue = Volley.newRequestQueue(this)

        val getRequest = object :  StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->

                val myObj = JSONObject(response)
                val responseCode = myObj["code"].toString().toInt()
                val responseMessage = myObj["message"].toString()
                val items = myObj["data"]
                val inputStream : InputStream = items.toString().byteInputStream()

                if (responseCode == 400){
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()
                }
                else{
//                    image = BitmapFactory.decodeFile(items.toString())
//                    imageView.setImageBitmap(image)
                    println("decoder: $items")
                    Toast.makeText(this, "${items}", Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                println("error: ${it.message}")
            }) {

        }
        queue.add(getRequest)
    }
}