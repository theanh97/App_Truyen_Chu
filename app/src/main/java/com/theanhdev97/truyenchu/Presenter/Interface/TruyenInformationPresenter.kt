package com.theanhdev97.truyenchu.Presenter.Interface

import android.content.Context
import com.theanhdev97.truyenchu.Model.Chapter
import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 29/07/2017.
 */
interface TruyenInformationPresenter {
    fun loadListChapters(truyen: Truyen)

    fun onShowListChaptersView(truyen: Truyen)

    fun onReadContinousChapter(chapter: Chapter, position: Int)

    fun onBackListTruyenByTypeView()

    fun onDownLoadTruyen(truyen: Truyen)

    fun downLoadTruyen(truyen: Truyen)

    fun onSelectedDownLoadTruyen()
}