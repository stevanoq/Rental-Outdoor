package com.example.jo.admin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.jo.LoginData
import com.example.jo.R
import com.example.jo.adapter.ItemsAdapter
import com.example.jo.models.Items
import org.json.JSONArray
import org.json.JSONObject

class AdminActivity : AppCompatActivity() {

    var arrayList = ArrayList<Items>()
    lateinit var mRecyclerView: RecyclerView
    lateinit var btn_add : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        mRecyclerView = findViewById(R.id.recycler_item)
        btn_add = findViewById(R.id.add_button)

        btn_add.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
            finish()
        }

        supportActionBar?.title = "Items data"

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()
        loadItems()
    }

    private fun loadItems() {
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()

        var url = "${LoginData.link}/item/index.php?state=getItems"
        val queue = Volley.newRequestQueue(this)

        val getRequest = object :  StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->

                val myObj = JSONObject(response)
                val responseCode = myObj["code"].toString().toInt()
                val responseMessage = myObj["message"].toString()
                val items = myObj["data"]
                val array = JSONArray(items.toString())

                if (responseCode == 400){
                    Toast.makeText(this, "$responseMessage", Toast.LENGTH_LONG).show()
                }
                else{
                    if (array?.length() == 0){
                        loading.dismiss()
                        Toast.makeText(this, "Items not found", Toast.LENGTH_SHORT).show()
                    }
                    for(i in 0 until array.length()){
                        val jsonObject = array.getJSONObject(i)
//                        array.get
                        println("jsonObject = ${jsonObject.getString("name")}")
                        println("index = $i")
                        arrayList.add(Items(
                            jsonObject.getString("name"),
                            jsonObject.getString("price"),
                            jsonObject.getString("description"),
                            jsonObject.getString("image"),
                            jsonObject.getString("uid")
                        ))

                        if (array?.length()!! - 1 == i){
                            loading.dismiss()
                            val adapter = ItemsAdapter(this, arrayList)
                            adapter.notifyDataSetChanged()
                            mRecyclerView.adapter = adapter
                        }
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