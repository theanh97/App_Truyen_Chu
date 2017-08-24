package com.theanhdev97.truyenchu.Presenter.Interface

import com.theanhdev97.truyenchu.Model.Chapter

/**
 * Created by DELL on 30/07/2017.
 */
interface ChapterPresenter {
    fun loadChapter(chapter: Chapter)

    fun saveReadingChapterPositionOfTruyen()

    fun onClickedChapterContentScreen(isHiding: Boolean)

    fun onSelectedBackListChapter()

    fun onSwipeScreen(isRight: Boolean)

    fun onSelectedLightSetting()

    fun onselectedTextSetting()

    fun onChangedBackgroundColor(colorID: Int)

    fun onChangedLightLevel()

    fun onChangedTextFont()

    fun onChangedTextSize(isPlus: Boolean)

    fun onChangedTextLineSpacingSize(isPlus: Boolean)

    fun onChangeFullScreen(isFullScreen:Boolean )
}