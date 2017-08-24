package com.theanhdev97.truyenchu.Presenter

import com.theanhdev97.truyenchu.Model.ListTruyenHelper
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListTruyenResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadTruyenInformationResult
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Model.TruyenInformationHelper
import com.theanhdev97.truyenchu.Presenter.Interface.ListTruyenByTypePresenter
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenchu.View.Interface.ListTruyenByTypeView
import com.theanhdev97.truyenhchu.Utils.Const

/**
 * Created by DELL on 28/07/2017.
 */
class ListTruyenByTypePresenterImpl(view: ListTruyenByTypeView,
                                    listTruyenModel: ListTruyenHelper,
                                    truyenInformationModel: TruyenInformationHelper)
    : ListTruyenByTypePresenter,
        OnLoadListTruyenResult, OnLoadTruyenInformationResult {


    var mView: ListTruyenByTypeView? = null
    var mListTruyenModel: ListTruyenHelper? = null
    var mTruyenInformationModel: TruyenInformationHelper? = null

    var mIsLoadMore = false
    var mIsHotType = true

    init {
        this.mView = view
        this.mListTruyenModel = listTruyenModel
        this.mListTruyenModel!!.setOnLoadListTruyenResult(this)

        this.mTruyenInformationModel = truyenInformationModel
        this.mTruyenInformationModel!!.setOnLoadTruyenResult(this)
    }

    override fun downLoadListTruyen(url: String) {
        mListTruyenModel!!.setListTruyenUrl(url)
        mListTruyenModel!!.getListTruyen()
    }

    override fun onLoadListTruyenSuccess(listTruyen: ArrayList<Truyen>) {
        // First load || Reload
        if (mIsLoadMore == false) {
            mView!!.setListTruyenToUI(listTruyen)
        }
        // Load more
        else {
            mIsLoadMore = false
            mView!!.addMoreListTruyenToUI(listTruyen)
        }
    }

    override fun onLoadListTruyenFailure() {
        mView!!.showViewError()
    }

    override fun onSelectedOneTruyen(truyen: Truyen) {
        downLoadTruyen(truyen)
    }

    override fun onSwipedToReloadListTruyen(url: String) {
        if (mIsHotType)
            downLoadListTruyen(url)
        else
            downLoadListTruyen(TruyenUtils.getHoanThanhUrl(url))
    }

    override fun onScrollToLastListTruyenPosition(url: String, pageNumber: Int) {
        mIsLoadMore = true
        if (mIsHotType)
            downLoadListTruyen(TruyenUtils.getPageNumberUrl(url, pageNumber))
        else
            downLoadListTruyen(
                    TruyenUtils.getPageNumberUrl(
                            TruyenUtils.getHoanThanhUrl(url),
                            pageNumber))
    }

    override fun onSelectedListTruyenType(url: String, isHotType: Boolean) {
        mIsHotType = isHotType
        if (mIsHotType)
            downLoadListTruyen(url)
        else
            downLoadListTruyen(TruyenUtils.getHoanThanhUrl(url))
    }

    override fun downLoadTruyen(truyen: Truyen) {
        mTruyenInformationModel!!.setTruyen(truyen)
        mTruyenInformationModel!!.getTruyen()
    }

    override fun onLoadTruyenInformationSuccess(truyen: Truyen) {
        mView!!.goToOneTruyen(truyen!!)
    }

    override fun onLoadTruyenInformationFailure() {
        mView!!.showViewError()
    }

    override fun onSelectedBack() {
        mView!!.backView()
    }
}