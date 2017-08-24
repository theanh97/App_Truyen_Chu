package com.theanhdev97.truyenchu.View.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 28/07/2017.
 */
interface ListTruyenByTypeView {
    fun initTransitionAnimation()

    fun initProgressDialog()

    fun setListTruyenToUI(listTruyen: ArrayList<Truyen>)

    fun showViewError()

    fun goToOneTruyen(truyen: Truyen)

    fun getAndInitListTruyenData()

    fun initToolbar()

    fun backView()

    fun setEventListener()

    fun addMoreListTruyenToUI(listTruyen:ArrayList<Truyen>)
}