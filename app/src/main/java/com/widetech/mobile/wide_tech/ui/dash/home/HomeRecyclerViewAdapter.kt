package com.widetech.mobile.wide_tech.ui.dash.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.widetech.mobile.wide_tech.DataAccess.DBLocal.modelsDB.Products
import com.widetech.mobile.wide_tech.R
import com.widetech.mobile.wide_tech.Utils.showImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.product_list_item.view.*

class HomeRecyclerViewAdapter  (
    private val context : Context,
    private var mValues: MutableList<Products>
) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val homeListViewModel : HomeViewModel

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Products
        }

        homeListViewModel = ViewModelProviders.of(context as FragmentActivity).get(
            HomeViewModel::class.java)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item: Products = mValues[position]

        holder.textview_name.text        = item.Name
        holder.textview_description.text = item.Description
        holder.textView_image.showImage(item.ImageUrl)

        setListeners(holder,item)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    private fun setListeners(holder : ViewHolder, item : Products){
        holder.mView
            .setOnClickListener {
            }
    }

    override fun getItemCount(): Int = mValues.size

    fun setData(listProducts : MutableList<Products>){
        this.mValues = listProducts
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val textview_name       : TextView = mView.tv_name_products
        val textview_description: TextView = mView.tv_description_products
        val textView_image      : CircleImageView = mView.ivc_avatar

    }
}