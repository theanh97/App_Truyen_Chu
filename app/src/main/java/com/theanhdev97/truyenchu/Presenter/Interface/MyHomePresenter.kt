package com.theanhdev97.truyenchu.Presenter.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 14/08/2017.
 */
interface MyHomePresenter {
    fun onSwipeToRefreshListTruyen()

    fun onSelectedTruyen(truyen: Truyen)

    fun loadTruyen(truyen: Truyen)

//    fun onSelectedListTruyenByType()
}