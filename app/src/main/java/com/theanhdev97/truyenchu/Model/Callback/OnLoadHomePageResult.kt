package com.theanhdev97.truyenchu.Model.Callback

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 06/08/2017.
 */
interface OnLoadHomePageResult {
    fun onLoadHomePageSuccess(listTruyenHot: ArrayList<Truyen>,
                              listTruyenHoanThanh: ArrayList<Truyen>,
                              listTruyenMoi: ArrayList<Truyen>)

    fun onLoadHomePageFailure()
}