package com.theanhdev97.truyenchu.View.Interface

import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Model.TruyenType

/**
 * Created by DELL on 09/08/2017.
 */
interface ListTypesView {
    fun setListTypeUI(listTypes: ArrayList<TruyenType>)

    fun showViewLoadListTypesFailure()

    fun showViewLoadListTruyenFailure()

    fun goToListTruyenView(listTruyen :ArrayList<Truyen>)

    fun initProgressDialog()

    fun setEventListener()
}