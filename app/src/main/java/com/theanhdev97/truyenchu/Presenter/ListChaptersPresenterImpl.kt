package com.theanhdev97.truyenchu.Presenter

import android.view.MenuItem
import com.theanhdev97.truyenchu.Model.Chapter
import com.theanhdev97.truyenchu.Model.ChapterHelper
import com.theanhdev97.truyenchu.Model.Callback.OnLoadChapterResult
import com.theanhdev97.truyenchu.Presenter.Interface.ListChaptersPresenter
import com.theanhdev97.truyenchu.View.Interface.ListChaptersView

/**
 * Created by DELL on 29/07/2017.
 */
class ListChaptersPresenterImpl(view: ListChaptersView, model: ChapterHelper)
    : ListChaptersPresenter, OnLoadChapterResult {
    var mView: ListChaptersView? = null
    var mModel: ChapterHelper? = null

    init {
        this.mView = view
        this.mModel = model
        this.mModel!!.setOnLoadChapterResult(this)
    }

    override fun onSelectedChapter(chapter: Chapter) {
        getSelectedChatper(chapter)
    }

    override fun onSelectedBackNavigation() {
        mView!!.backToTruyenInformation()
    }

    override fun getSelectedChatper(chapter: Chapter) {
        mModel!!.setChapter(chapter)
        mModel!!.getChapter()
    }

    override fun onLoadChapterSuccess(chapter: Chapter) {
        mView!!.goToSelectedChapter(chapter)
    }

    override fun onSelectedSortMenu(menu: MenuItem, isDescreased: Boolean) {
        // INSCREASE
        if (isDescreased)
            mView!!.showInscreaseView(menu)
        // DESCREASE
        else
            mView!!.showDescreaseView(menu)
    }

    override fun onLoadChapterFailure() {
        mView!!.showViewError()
    }

    override fun onSelectedContinousChapter(chapter: Chapter) {
        getSelectedChatper(chapter)
    }
}