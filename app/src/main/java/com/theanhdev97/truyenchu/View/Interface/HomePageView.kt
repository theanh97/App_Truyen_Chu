package com.theanhdev97.truyenchu.View.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 09/08/2017.
 */
interface HomePageView {
    fun setListTruyenToView(listTruyenHot: ArrayList<Truyen>,
                            listTruyenHoanThanh: ArrayList<Truyen>,
                            listTruyenMoi: ArrayList<Truyen>)

    fun setProgressDialog()

    fun setEventListener()

    fun repairUI()

    fun goToOneTruyen(truyen: Truyen)

    fun showViewHomePageError()

    fun showViewLoadTruyenError()

    fun showViewLoadListTruyenError()

    fun goToListTruyensView(listTruyens :ArrayList<Truyen>)

}