package com.theanhdev97.truyenchu.Model.Callback

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 13/08/2017.
 */
interface OnSearchTruyenResult {
    fun onSearchTruyenSuccess(listTruyen: ArrayList<Truyen>, maxPageSearchSize: Int)

    fun onSearchTruyenFailure()
}