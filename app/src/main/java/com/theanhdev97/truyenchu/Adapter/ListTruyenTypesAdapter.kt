package com.theanhdev97.truyenchu.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.theanhdev97.truyenchu.Model.TruyenType
import com.theanhdev97.truyenchu.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.truyen_by_type_row.view.*
import kotlinx.android.synthetic.main.truyen_type_row.view.*

/**
 * Created by DELL on 09/07/2017.
 */
class ListTruyenTypesAdapter(context: Context, listTruyenTypes: ArrayList<TruyenType>) :
        RecyclerView.Adapter<ListTruyenTypesAdapter.itemHolder>() {
    var mContext: Context? = null

    var mListTruyenTypes: ArrayList<TruyenType>? = null
    var mClickListener: MyClickListener? = null

    init {
        this.mContext = context
        this.mListTruyenTypes = listTruyenTypes
    }

    override fun onBindViewHolder(holder: ListTruyenTypesAdapter.itemHolder?, position: Int) {
        var truyenType = mListTruyenTypes!![position]
        holder!!.name!!.text = truyenType.name
    }

    override fun getItemCount(): Int {
        return mListTruyenTypes!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListTruyenTypesAdapter.itemHolder? {
        var view = LayoutInflater.from(mContext).inflate(R.layout.truyen_type_row, parent, false)
        return itemHolder(view)
    }

    inner class itemHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        override fun onClick(v: View?) {
            mClickListener!!.OnItemClickListener(adapterPosition, v!!)
        }

        var name: TextView? = null

        init {
            name = view.tv_name
            view.setOnClickListener(this)
        }
    }

    fun setOnClickListener(onClickListener: MyClickListener) {
        this.mClickListener = onClickListener
    }

    interface MyClickListener {
        fun OnItemClickListener(position: Int, view: View)
    }
}