package com.theanhdev97.truyenchu.View.Interface

import com.theanhdev97.truyenchu.Model.Chapter
import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 29/07/2017.
 */
interface TruyenInformationView {
    fun initTransitionAnimation()

    fun showDownLoadDialog()

    fun initProgressDialog()

    fun getTruyen()

    fun initToolbar()

    fun backToListTruyenByTypeView()

    fun setTransitionAnimation()

    fun showLoadListChapterViewError()

    fun showViewLoadContinousChapterError()

    fun registerEvent()

    fun setTruyenToUI(truyen: Truyen)

    fun goToListChaptersView(truyen: Truyen)

    fun goToContinousChapterView(chapter : Chapter)

    fun showDownLoadTruyenSuccess()

    fun showDownLoadTruyenError()
}