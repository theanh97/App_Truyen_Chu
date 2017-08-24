package com.theanhdev97.truyenchu.View.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 14/08/2017.
 */
interface MyHomeView {
//    fun goToListTruyenView()

    fun initProgressDialog()

    fun setEventListener()

    fun repairUI()

    fun getData()

    fun getListTruyenDownLoad(): ArrayList<Truyen>

    fun getListTruyenReading(): ArrayList<Truyen>

    fun updateListTruyenToUI()

    fun showViewLoadTruyenError()

    fun goToOneTruyenView(truyen: Truyen)
}