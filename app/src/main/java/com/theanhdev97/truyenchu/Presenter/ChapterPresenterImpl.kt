package com.theanhdev97.truyenchu.Presenter

import com.theanhdev97.truyenchu.Model.Chapter
import com.theanhdev97.truyenchu.Model.ChapterHelper
import com.theanhdev97.truyenchu.Model.Callback.OnLoadChapterResult
import com.theanhdev97.truyenchu.Presenter.Interface.ChapterPresenter
import com.theanhdev97.truyenchu.View.Interface.ChapterView

/**
 * Created by DELL on 30/07/2017.
 */
class ChapterPresenterImpl(view: ChapterView, model: ChapterHelper)
    : ChapterPresenter, OnLoadChapterResult {

    var mView: ChapterView? = null
    var mModel: ChapterHelper? = null

    init {
        this.mView = view
        this.mModel = model
        this.mModel!!.setOnLoadChapterResult(this)
    }

    override fun onLoadChapterSuccess(chapter: Chapter) {
        mView!!.reloadNewChapterUI(chapter)
        mView!!.saveReadingChapterPosition()
    }

    override fun saveReadingChapterPositionOfTruyen() {
        mView!!.saveReadingChapterPosition()
    }

    override fun onLoadChapterFailure() {
        mView!!.showViewError()
    }

    override fun onSelectedBackListChapter() {
        mView!!.backToListChapters()
    }

    override fun onSelectedLightSetting() {
        mView!!.showLightSetting()
    }

    override fun onselectedTextSetting() {
        mView!!.showTextSetting()
    }

    override fun onChangedBackgroundColor(colorID: Int) {
        mView!!.setBackgroundColor(colorID)
    }

    override fun onChangedLightLevel() {
        mView!!.setLightLevel()
    }

    override fun onChangedTextFont() {
        mView!!.setTextFont()
    }

    override fun loadChapter(chapter: Chapter) {
        mModel!!.setChapter(chapter)
        mModel!!.getChapter()
    }

    override fun onSwipeScreen(isLeft: Boolean) {
        if (isLeft == false) {
            var nextChapter = mView!!.checkHasNextChapter()
            if (nextChapter != null) {
                loadChapter(nextChapter)
            } else
                mView!!.showNotHasNextChapter()
        } else {
            var prevChapter = mView!!.checkHasPrevChapter()
            if (prevChapter != null) {
                loadChapter(prevChapter)
            } else
                mView!!.showNotHasPrevChapter()
        }
    }

    override fun onClickedChapterContentScreen(isHiding: Boolean) {
        if (isHiding)
            mView!!.showMenuSetting()
        else
            mView!!.hideAllBottomShettingDialog()
    }

    override fun onChangedTextLineSpacingSize(isPlus: Boolean) {
        if (isPlus)
            mView!!.inscreaseTextLineSpacingSize()
        else
            mView!!.descreaseTextLineSpacingSize()
    }

    override fun onChangedTextSize(isPlus: Boolean) {
        if (isPlus)
            mView!!.inscreaseTextSize()
        else
            mView!!.descreaseTextSize()
    }

    override fun onChangeFullScreen(isFullScreen: Boolean) {
        mView!!.setFullScreen(isFullScreen)
    }

}