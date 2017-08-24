package com.theanhdev97.truyenchu.View.Interface

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 06/08/2017.
 */
interface HomeView {
    fun initTransitionAnimation()

    fun requestPermissions()

    fun initDialog()

    fun initToolbar()

    fun initViewPager()

    fun setEventListener()

    fun goToHomePageView(isScrolling : Boolean)

    fun goToListTypesView(isScrolling : Boolean)

    fun goToMyHomeView(isScrolling : Boolean)

    fun goToSearchView()

    fun goToRatingAppView()

    fun exitApp()

    fun showPressBackAgainToExitApp()

    fun showInformationDialog()
}