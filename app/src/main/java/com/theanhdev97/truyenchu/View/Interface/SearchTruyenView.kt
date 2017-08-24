package com.theanhdev97.truyenchu.View.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 13/08/2017.
 */
interface SearchTruyenView {
    fun initTransitionAnimation()

    fun initToolbar()

    fun hideVirtualKeyboard()

    fun removeSearchKeyWords()

    fun backToHome()

    fun setListTruyenToUI(listTryuen:ArrayList<Truyen>)

    fun showViewSearchTruyenError()

    fun addMoreListTruyenToUI(listTruyens :ArrayList<Truyen>)

    fun setEventListener()

    fun repairUI()

    fun setEmptyListTruyensToUI()

    fun initProgressDialog()

    fun showViewLoadTruyenError()

    fun goToOneTruyenView(truyen :Truyen)
}