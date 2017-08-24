package com.theanhdev97.truyenchu.View.Interface

import android.view.MenuItem
import com.theanhdev97.truyenchu.Model.Chapter

/**
 * Created by DELL on 29/07/2017.
 */
interface ListChaptersView {
    fun initTransitionAnimation()

    fun setListChaptersToUI()

    fun showViewError()

    fun goToSelectedChapter(chapter: Chapter)

    fun initToolbar()

    fun backToTruyenInformation()

    fun initProgressDialog()

    fun getData()

    fun setEventListener()

    fun showDescreaseView(menu: MenuItem)

    fun showInscreaseView(menu: MenuItem)
}