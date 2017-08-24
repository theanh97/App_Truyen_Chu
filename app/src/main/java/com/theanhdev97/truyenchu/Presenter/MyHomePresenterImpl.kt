package com.theanhdev97.truyenchu.Presenter

import com.theanhdev97.truyenchu.Model.Callback.OnLoadTruyenInformationResult
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Model.TruyenInformationHelper
import com.theanhdev97.truyenchu.Presenter.Interface.MyHomePresenter
import com.theanhdev97.truyenchu.View.Interface.MyHomeView
import com.theanhdev97.truyenhchu.Utils.Const

/**
 * Created by DELL on 14/08/2017.
 */
class MyHomePresenterImpl(view: MyHomeView,
                          model: TruyenInformationHelper)
    : MyHomePresenter,
        OnLoadTruyenInformationResult {

    // MVP
    var mView: MyHomeView? = null
    var mModel: TruyenInformationHelper? = null

    init {
        this.mView = view
        this.mModel = model
        this.mModel!!.setOnLoadTruyenResult(this)
    }

    override fun onSwipeToRefreshListTruyen() {
        mView!!.updateListTruyenToUI()
    }

    override fun onLoadTruyenInformationSuccess(truyen: Truyen) {
        mView!!.goToOneTruyenView(truyen)
    }

    override fun onLoadTruyenInformationFailure() {
        mView!!.showViewLoadTruyenError()
    }

    override fun onSelectedTruyen(truyen: Truyen) {
        loadTruyen(truyen)
    }

    override fun loadTruyen(truyen: Truyen) {
        mModel!!.setTruyen(truyen)
        mModel!!.getTruyen()
    }

//    override fun onSelectedListTruyenByType() {
//        mView!!.goToListTruyenView()
//    }
}