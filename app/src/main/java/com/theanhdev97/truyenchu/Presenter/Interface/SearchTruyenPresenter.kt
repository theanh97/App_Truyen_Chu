package com.theanhdev97.truyenchu.Presenter.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 13/08/2017.
 */
interface SearchTruyenPresenter {
    fun onSelectedBackNavigation()

    fun onSeletedClearKeyWords()

    fun onSearchKeyWordsChanged(keyWords: String)

    fun getListTruyenByKeyWords(keyWords: String)

    fun onSwipeToLoadMoreTruyens()

    fun checkLimitSearchPages():Boolean

    fun onClickSearch(keyWords: String)

    fun onSelectedTruyen(truyen : Truyen)

    fun loadTruyen(truyen : Truyen)
}