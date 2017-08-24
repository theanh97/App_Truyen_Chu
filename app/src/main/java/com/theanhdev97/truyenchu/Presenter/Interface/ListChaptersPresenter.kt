package com.theanhdev97.truyenchu.Presenter.Interface

import android.view.MenuItem
import com.theanhdev97.truyenchu.Model.Chapter

/**
 * Created by DELL on 29/07/2017.
 */
interface ListChaptersPresenter {
    fun onSelectedChapter(chapter: Chapter)

    fun getSelectedChatper(chapter:Chapter)

    fun onSelectedBackNavigation()

    fun onSelectedSortMenu(menu: MenuItem, isDescreasing: Boolean)

    fun onSelectedContinousChapter(chapter:Chapter)
}

