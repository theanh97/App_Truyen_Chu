package com.theanhdev97.truyenchu.Presenter.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 06/08/2017.
 */
interface HomePagePresenter {
    fun loadHomePageData()

    fun onRefreshData()

    fun onSelectedTruyen(truyen: Truyen)

    fun onSelectedListTruyenByType(url: String)

    fun getListTruyen(url: String)

}