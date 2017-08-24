package com.theanhdev97.truyenchu.Presenter

import android.os.Handler
import com.theanhdev97.truyenchu.Presenter.Interface.HomePresenter
import com.theanhdev97.truyenchu.View.Interface.HomeView

/**
 * Created by DELL on 06/08/2017.
 */
class HomePresenterImpl(view: HomeView)
    : HomePresenter {

    var mView: HomeView? = null
    var isExit = 0

    init {
        this.mView = view
    }

    override fun onSelectedAnotherPage(position: Int, isScrolling: Boolean) {
        when (position) {
            0 -> mView!!.goToHomePageView(isScrolling)
            1 -> mView!!.goToListTypesView(isScrolling)
            2 -> mView!!.goToMyHomeView(isScrolling)
        }
    }

    override fun onSelectedSearchMenu() {
        mView!!.goToSearchView()
    }

    override fun onBackPressed() {
        if (isExit == 1)
            mView!!.exitApp()
        else {
            isExit++
            mView!!.showPressBackAgainToExitApp()
            Handler().postDelayed({
                isExit = 0
            }, 3000)
        }
    }

    override fun onSelectedInformationMenu() {
        mView!!.showInformationDialog()
    }

    override fun onSelectedRatingApp(){
        mView!!.goToRatingAppView()
    }
}