package com.theanhdev97.truyenchu.Presenter

import android.content.Context
import com.theanhdev97.truyenchu.Model.*
import com.theanhdev97.truyenchu.Model.Callback.OnDownLoadTruyenResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadChapterResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListChaptersResult
import com.theanhdev97.truyenchu.Presenter.Interface.TruyenInformationPresenter
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenchu.View.Interface.TruyenInformationView
import com.theanhdev97.truyenhchu.Utils.InternalStorage

/**
 * Created by DELL on 29/07/2017.
 */
class TruyenInformationPresenterImpl(
        view: TruyenInformationView,
        listChapterModel: ListChaptersHelper,
        downLoadTruyenModel: DownLoadTruyenHelper,
        chapterModel: ChapterHelper)
    : TruyenInformationPresenter,
        OnLoadListChaptersResult, OnDownLoadTruyenResult, OnLoadChapterResult {


    var mView: TruyenInformationView? = null
    var mListChaptersModel: ListChaptersHelper? = null
    var mDownLoadTruyenModel: DownLoadTruyenHelper? = null
    var mChapterModel: ChapterHelper? = null

    init {
        this.mView = view

        this.mListChaptersModel = listChapterModel
        this.mListChaptersModel!!.setOnLoadListChapterResult(this)

        this.mDownLoadTruyenModel = downLoadTruyenModel
        this.mDownLoadTruyenModel!!.setOnDownTruyenListener(this)

        this.mChapterModel = chapterModel
        this.mChapterModel!!.setOnLoadChapterResult(this)
    }

    override fun loadListChapters(truyen: Truyen) {
//        mListChaptersModel!!.setTruyen(truyen)
//        mListChaptersModel!!.getListChapter()
        mView!!.goToListChaptersView(truyen)
    }

    override fun onBackListTruyenByTypeView() {
        mView!!.backToListTruyenByTypeView()
    }

    // load view success
    override fun onLoadListChapterSuccess(truyen: Truyen) {
        loadListChapters(truyen)
    }

    override fun onLoadListChapterFailure() {
        mView!!.showLoadListChapterViewError()
    }

    override fun onShowListChaptersView(truyen: Truyen) {
        loadListChapters(truyen)
    }

    override fun onDownLoadTruyen(truyen: Truyen) {
        downLoadTruyen(truyen)
    }

    override fun downLoadTruyen(truyen: Truyen) {
        mDownLoadTruyenModel!!.setTruyen(truyen)
        mDownLoadTruyenModel!!.downLoadTruyen()
    }

    override fun onReadContinousChapter(chapter: Chapter, position: Int) {
        mChapterModel!!.getChapterByPosition(chapter, position)
    }

    override fun onDownLoadTruyenSuccess() {
        mView!!.showDownLoadTruyenSuccess()
    }

    override fun onDownLoadTruyenFailure() {
        mView!!.showDownLoadTruyenError()
    }

    override fun onLoadChapterSuccess(chapter: Chapter) {
        mView!!.goToContinousChapterView(chapter!!)
    }

    override fun onLoadChapterFailure() {
        mView!!.showViewLoadContinousChapterError()
    }

    override fun onSelectedDownLoadTruyen() {
        mView!!.showDownLoadDialog()
    }
}