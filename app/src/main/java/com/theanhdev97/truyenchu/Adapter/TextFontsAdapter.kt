package com.theanhdev97.truyenhot.Adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.theanhdev97.truyenchu.R
import kotlinx.android.synthetic.main.text_font_row.view.*

/**
 * Created by DELL on 27/07/2017.
 */
class TextFontsAdapter(context: Activity, listFonts: ArrayList<String>) : BaseAdapter() {
    var mContext: Activity? = null
    var listFonts: ArrayList<String>? = null

    init {
        this.mContext = context
        this.listFonts = listFonts
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = mContext!!.layoutInflater.inflate(R.layout.text_font_row, parent, false)
        view.tv_text_font.text = listFonts!!.get(position)
        return view!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = mContext!!.layoutInflater.inflate(R.layout.text_font_row, parent, false)
        view.tv_text_font.text = listFonts!!.get(position)
        return view!!
    }

    override fun getItem(position: Int): Any {
        return listFonts!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listFonts!!.size
    }
}