package com.theanhdev97.truyenchu.Utils

import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import com.theanhdev97.truyenchu.R


/**
 * Created by DELL on 02/08/2017.
 */
class SimpleDividerItemDecoration(context :Context) : RecyclerView.ItemDecoration() {
    var mDivider: Drawable? = null

   init{
       mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
   }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight

            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }
    }
}