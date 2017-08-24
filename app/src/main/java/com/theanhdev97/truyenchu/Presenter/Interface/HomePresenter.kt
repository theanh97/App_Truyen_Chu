package com.theanhdev97.truyenchu.Presenter.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 06/08/2017.
 */
interface HomePresenter {
    fun onSelectedAnotherPage(position: Int, isScrolling: Boolean)

    fun onSelectedSearchMenu()

    fun onBackPressed()

    fun onSelectedInformationMenu()

    fun onSelectedRatingApp()

}