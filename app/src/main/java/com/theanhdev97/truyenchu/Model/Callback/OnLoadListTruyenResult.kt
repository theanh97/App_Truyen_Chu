package com.theanhdev97.truyenchu.Model.Callback

/**
 * Created by DELL on 28/07/2017.
 */
interface OnLoadListTruyenResult {
    fun onLoadListTruyenSuccess(listTruyen: ArrayList<com.theanhdev97.truyenchu.Model.Truyen>)

    fun onLoadListTruyenFailure()
}