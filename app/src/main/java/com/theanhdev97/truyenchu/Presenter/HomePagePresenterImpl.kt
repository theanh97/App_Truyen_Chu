package com.theanhdev97.truyenchu.Presenter

import android.util.Log
import com.theanhdev97.truyenchu.Model.Callback.OnLoadHomePageResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListTruyenResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadTruyenInformationResult
import com.theanhdev97.truyenchu.Model.HomePageHelper
import com.theanhdev97.truyenchu.Model.ListTruyenHelper
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Model.TruyenInformationHelper
import com.theanhdev97.truyenchu.Presenter.Interface.HomePagePresenter
import com.theanhdev97.truyenchu.Presenter.Interface.HomePresenter
import com.theanhdev97.truyenchu.View.Interface.HomePageView
import com.theanhdev97.truyenchu.View.Interface.HomeView
import com.theanhdev97.truyenhchu.Utils.Const

/**
 * Created by DELL on 06/08/2017.
 */
class HomePagePresenterImpl(view: HomePageView,
                        homePageModel: HomePageHelper,
                        truyenInformationModel: TruyenInformationHelper,
                        listTruyenHelper: ListTruyenHelper) :
        HomePagePresenter,
        OnLoadHomePageResult,
        OnLoadTruyenInformationResult,
        OnLoadListTruyenResult {


    var mView: HomePageView? = null
    var mHomePageModel: HomePageHelper? = null
    var mTruyenInformationModel: TruyenInformationHelper? = null
    var mListTruyenModel: ListTruyenHelper? = null

    init {
        this.mView = view

        this.mTruyenInformationModel = truyenInformationModel
        this.mTruyenInformationModel!!.setOnLoadTruyenResult(this)

        this.mHomePageModel = homePageModel
        this.mHomePageModel!!.setOnLoadHomePageListener(this)

        this.mListTruyenModel = listTruyenHelper
        this.mListTruyenModel!!.setOnLoadListTruyenResult(this)
        loadHomePageData()
    }

    override fun loadHomePageData() {
        this.mHomePageModel!!.loadHomePage()
    }

    override fun onLoadHomePageSuccess(listTruyenHot: ArrayList<Truyen>,
                                       listTruyenHoanThanh: ArrayList<Truyen>,
                                       listTruyenMoi: ArrayList<Truyen>) {
        mView!!.setListTruyenToView(listTruyenHot, listTruyenHoanThanh, listTruyenMoi)
    }

    override fun onRefreshData() {
        loadHomePageData()
    }

    override fun onSelectedTruyen(truyen: Truyen) {
        mTruyenInformationModel!!.setTruyen(truyen)
        mTruyenInformationModel!!.getTruyen()
    }

    override fun onSelectedListTruyenByType(url: String) {
        getListTruyen(url)
    }



    override fun getListTruyen(url: String) {
        mListTruyenModel!!.setListTruyenUrl(url)
        mListTruyenModel!!.getListTruyen()
    }

    override fun onLoadHomePageFailure() {
        mView!!.showViewHomePageError()
    }

    override fun onLoadTruyenInformationSuccess(truyen: Truyen) {
        mView!!.goToOneTruyen(truyen)
    }

    override fun onLoadListTruyenSuccess(listTruyen: ArrayList<Truyen>) {
        mView!!.goToListTruyensView(listTruyen)
    }

    override fun onLoadListTruyenFailure() {
        mView!!.showViewLoadListTruyenError()
    }

    override fun onLoadTruyenInformationFailure() {
        mView!!.showViewLoadTruyenError()
    }
}