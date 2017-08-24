package com.theanhdev97.truyenchu.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.truyen_by_type_row.view.*

/**
 * Created by DELL on 09/07/2017.
 */
class TruyenByTypeAdapter(context: Context, listTruyen: ArrayList<Truyen>) :
        RecyclerView.Adapter<TruyenByTypeAdapter.itemHolder>() {

    var mContext: Context? = null
    var mListTruyen: ArrayList<Truyen>? = null
    var mClickListener: MyClickListener? = null

    init {
        this.mContext = context
        this.mListTruyen = listTruyen
    }

    override fun onBindViewHolder(holder: itemHolder?, position: Int) {
        var truyen = mListTruyen!![position]
        holder!!.title!!.text = truyen.title
        holder!!.author!!.text = truyen.author
        holder!!.maxChapter!!.text = "Chương : ${truyen.maxChapter}"
        Glide.with(mContext)
                .load(truyen.imageOne)
                .override(400, 180)
                .placeholder(R.drawable.book_replace)
                .into(holder!!.image)
    }

    override fun getItemCount(): Int {
        return mListTruyen!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): itemHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.truyen_by_type_row, parent, false)
        return itemHolder(view)
    }

    inner class itemHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        override fun onClick(v: View?) {
            mClickListener!!.OnItemClickListener(adapterPosition, v!!)
        }

        var image: ImageView? = null
        var title: TextView? = null
        var author: TextView? = null
        var maxChapter: TextView? = null

        init {
            image = view.imv_image
            title = view.tv_title
            author = view.tv_author
            maxChapter = view.tv_max_chapter
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