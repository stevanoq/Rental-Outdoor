package com.example.jo.admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.jo.LoginData
import com.example.jo.R
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException


class AddItemActivity : AppCompatActivity() {

    lateinit var btn_cancle : Button
    lateinit var btn_confirm : Button
    lateinit var image : ImageView
    lateinit var et_name : EditText
    lateinit var et_price : EditText
    lateinit var et_description : EditText

    lateinit var bitmap : Bitmap
    val PICK_IMAGE_REQUEST = 1
    val UPLOAD_KEY = "image"
    val UPLOAD_URL = "${LoginData.link}/item/index.php"

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        image = findViewById(R.id.item_image)
        btn_confirm = findViewById(R.id.button_add_item)
        btn_cancle = findViewById(R.id.button_cancle_add)
        et_name = findViewById(R.id.item_name)
        et_price = findViewById(R.id.item_price)
        et_description = findViewById(R.id.item_description)

        image.setOnClickListener{
            showFileChooser()
        }

        if (LoginData.editable){
            getData()
            btn_confirm.setText("Update")
        }

        btn_confirm.setOnClickListener {

            if (LoginData.editable){
                println("Masuk editable")
                updateData()
            }
            else{
                println("Masuk upload")
                uploadItem()
            }
        }

        btn_cancle.setOnClickListener {
            LoginData.editable = false
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateData() {
        val name = et_name.text.toString()
        val price = et_price.text.toString()
        val description = et_description.text.toString()
        if (name.isEmpty() && price.isEmpty() && description.isEmpty()){
            return Toast.makeText(this, "Isi datamu bangsat", Toast.LENGTH_SHORT).show()
        }
        class UploadImage : AsyncTask<Bitmap?, Void?, String?>() {
            var loading: ProgressDialog? = null
            var rh = RequestHandler()

            override fun doInBackground(vararg params: Bitmap?): String? {

                val bitmap = params[0]
                val uploadImage = bitmap?.let { getStringImage(it) }
                val data =
                    HashMap<String, String?>()

                data[UPLOAD_KEY] = uploadImage
                data["name"] = name
                data["price"] = price
                data["description"] = description
                data["uid"] = LoginData.key
                return rh.sendPostRequest("${LoginData.link}/item/index.php", data)
            }

            override fun onPreExecute() {
                super.onPreExecute()
                loading = ProgressDialog.show(
                    this@AddItemActivity,
                    "Uploading Image",
                    "Please wait...",
                    true,
                    true
                )
            }

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                loading!!.dismiss()
                Toast.makeText(applicationContext, "Successfull Updating Item", Toast.LENGTH_LONG).show()
                LoginData.editable = false
                val intent = Intent(applicationContext, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }

    }
        val ui = UploadImage()
        ui.execute(bitmap)

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

                if (responseCode == 400){
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()
                }
                else{
                    val items = JSONObject(myObj["data"].toString())
                    loading.dismiss()
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()

                    Glide.with(this)
                        .load("${LoginData.link}${items["image"].toString().substring(1, items["image"].toString().length)}")
                        .into(image)

                    et_name.setText(items["name"].toString())
                    et_price.setText(items["price"].toString())
                    et_description.setText(items["description"].toString())

                }

            }, Response.ErrorListener {
                Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show()
                println("error: ${it.message}")
            }) {

        }
        queue.add(getRequest)
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getStringImage(bmp: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
    }
    private fun uploadItem() {
        val name = et_name.text.toString()
        val price = et_price.text.toString()
        val description = et_description.text.toString()
        if (name.isEmpty() && price.isEmpty() && description.isEmpty()){
            return Toast.makeText(this, "Isi datamu bangsat", Toast.LENGTH_SHORT).show()
        }
        class UploadImage : AsyncTask<Bitmap?, Void?, String?>() {
            var loading: ProgressDialog? = null
            var rh = RequestHandler()

            override fun doInBackground(vararg params: Bitmap?): String? {

                val bitmap = params[0]
                val uploadImage = bitmap?.let { getStringImage(it) }
                val data =
                    HashMap<String, String?>()
                data[UPLOAD_KEY] = uploadImage
                data["name"] = name
                data["price"] = price
                data["description"] = description
                return rh.sendPostRequest(UPLOAD_URL, data)
            }

            override fun onPreExecute() {
                super.onPreExecute()
                loading = ProgressDialog.show(
                    this@AddItemActivity,
                    "Uploading Image",
                    "Please wait...",
                    true,
                    true
                )
            }

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                loading!!.dismiss()
                Toast.makeText(applicationContext, "Successfull Adding Item", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        val ui = UploadImage()
        ui.execute(bitmap)
    }


}