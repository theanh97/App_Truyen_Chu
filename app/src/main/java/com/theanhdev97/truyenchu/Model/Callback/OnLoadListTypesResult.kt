package com.theanhdev97.truyenchu.Model.Callback

import com.theanhdev97.truyenchu.Model.TruyenType

/**
 * Created by DELL on 09/08/2017.
 */
interface OnLoadListTypesResult {
    fun onLoadListTypesSuccess(listTruyenTypes: ArrayList<TruyenType>)

    fun onLoadListTypesFailure()
}