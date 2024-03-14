package com.example.jo.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jo.LoginData
import com.example.jo.R
import com.example.jo.listener.IRecyclerClickListener
import com.example.jo.models.Items
import java.lang.StringBuilder
import java.util.Base64

class ItemsAdapter(
    private val context: Context,
    private val arrayList: ArrayList<Items>)
    : RecyclerView.Adapter<ItemsAdapter.Holder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false))
    }

    override fun getItemCount(): Int = arrayList!!.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {

        Glide.with(context)
            .load("${LoginData.link}${
                arrayList[position].image!!.substring(
                    1,
                    arrayList[position].image!!.length
                )
            }")
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuilder().append(arrayList[position].name)
        holder.txtPrice!!.text = StringBuilder("Rp.").append(arrayList[position].price)
        holder.txtDiscribe!!.text = StringBuilder().append(arrayList[position].description)

        holder.setOnClickListener(object :IRecyclerClickListener{
            override fun onItemClickListener(view: View?, position: Int) {
                addToCart(arrayList[position])
            }
        })
    }

    private fun addToCart(any: Any) {
        TODO("Not yet implemented")
    }

    class Holder( itemView:View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var imageView: ImageView?=null
        var txtName: TextView?=null
        var txtPrice: TextView?=null
        var txtDiscribe: TextView?=null

        private var clickListener: IRecyclerClickListener?=null

        fun setOnClickListener(clickListener: IRecyclerClickListener){
            this.clickListener = clickListener;
        }

        init {
            imageView = itemView.findViewById(R.id.imageView) as ImageView
            txtName = itemView.findViewById(R.id.item_name) as TextView
            txtPrice = itemView.findViewById(R.id.item_price) as TextView
            txtDiscribe = itemView.findViewById(R.id.item_decript) as TextView

            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            println("click")
        }
    }



}