package com.theanhdev97.truyenchu.Presenter.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 28/07/2017.
 */
interface ListTruyenByTypePresenter {
    fun downLoadListTruyen(url: String)

    fun onSelectedOneTruyen(truyen: Truyen)

    fun downLoadTruyen(truyen: Truyen)

    fun onSelectedBack()

    fun onSwipedToReloadListTruyen(url: String)

    fun onScrollToLastListTruyenPosition(url: String, pageNumber: Int)

    fun onSelectedListTruyenType(url: String, isHotType: Boolean)
}