package com.theanhdev97.truyenchu.Model

import android.content.Context

/**
 * Created by DELL on 18/08/2017.
 */
class TruyenHolder {
    private var mListTruyenReading: ArrayList<Truyen>? = null
    private var mListTruyenDownloaded: ArrayList<Truyen>? = null
    private var mCurTruyen: Truyen? = null

    fun getListReading(): ArrayList<Truyen>{
        var list = ArrayList<Truyen>()
        if (mListTruyenReading != null && mListTruyenReading!!.size > 0)
            list.addAll(mListTruyenReading!!)
        return list
    }

    fun setListReading(listTruyenReading: ArrayList<Truyen>?) {
        if (listTruyenReading != null) {
            mListTruyenReading = ArrayList<Truyen>()
            mListTruyenReading!!.addAll(listTruyenReading)
        }
    }

    fun getListDownloaded(): ArrayList<Truyen> {
        var list = ArrayList<Truyen>()
        if (mListTruyenDownloaded != null && mListTruyenDownloaded!!.size > 0)
            list.addAll(mListTruyenDownloaded!!)
        return list
    }

    fun setListDownloaded(listTruyenDownloaded: ArrayList<Truyen>?) {
        if (listTruyenDownloaded != null) {
            mListTruyenDownloaded = ArrayList<Truyen>()
            mListTruyenDownloaded!!.addAll(listTruyenDownloaded)
        }
    }

    fun getCurTruyen(): Truyen? = mCurTruyen!!.copy()

    fun setCurTruyen(truyen: Truyen?) {
        if (truyen != null)
            this.mCurTruyen = truyen!!.copy()
    }

    companion object {
        private var sTruyenHolder: TruyenHolder? = null
        fun getInstance(): TruyenHolder {
            if (sTruyenHolder == null) {
                sTruyenHolder = TruyenHolder()
            }
            return sTruyenHolder!!
        }
    }
}