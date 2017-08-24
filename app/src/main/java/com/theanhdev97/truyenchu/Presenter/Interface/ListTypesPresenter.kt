package com.theanhdev97.truyenchu.Presenter.Interface

/**
 * Created by DELL on 09/08/2017.
 */
interface ListTypesPresenter {
    fun getListTypes()

    fun onSelectedListTruyen(url: String)

    fun getListTruyen(url: String)

    fun onSwipeToReloadListTypes()
}