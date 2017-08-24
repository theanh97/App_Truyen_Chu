package com.theanhdev97.truyenchu.Presenter

import com.theanhdev97.truyenchu.Model.Callback.OnLoadListTruyenResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListTypesResult
import com.theanhdev97.truyenchu.Model.ListTruyenHelper
import com.theanhdev97.truyenchu.Model.ListTypesHelper
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Model.TruyenType
import com.theanhdev97.truyenchu.Presenter.Interface.ListTypesPresenter
import com.theanhdev97.truyenchu.View.Interface.ListTypesView

/**
 * Created by DELL on 09/08/2017.
 */
class ListTypesPresenterImpl(view: ListTypesView,
                             listTruyenModel: ListTruyenHelper,
                             listTypesModel: ListTypesHelper) :
        ListTypesPresenter,
        OnLoadListTruyenResult,
        OnLoadListTypesResult {

    var mView: ListTypesView? = null
    var mListTruyenModel: ListTruyenHelper? = null
    var mListTypesModel: ListTypesHelper? = null

    init {
        this.mView = view
        this.mListTruyenModel = listTruyenModel
        this.mListTruyenModel!!.setOnLoadListTruyenResult(this)
        this.mListTypesModel = listTypesModel
        this.mListTypesModel!!.setOnLoadListTypesListener(this)

        getListTypes()
    }

    override fun getListTypes() {
        mListTypesModel!!.getListTypes()
    }

    override fun onLoadListTruyenSuccess(listTruyen: ArrayList<Truyen>) {
        mView!!.goToListTruyenView(listTruyen)
    }

    override fun onSelectedListTruyen(url: String) {
        getListTruyen(url)
    }

    override fun onSwipeToReloadListTypes() {
        getListTypes()
    }

    override fun getListTruyen(url: String) {
        mListTruyenModel!!.setListTruyenUrl(url)
        mListTruyenModel!!.getListTruyen()
    }

    override fun onLoadListTruyenFailure() {
        mView!!.showViewLoadListTruyenFailure()
    }

    override fun onLoadListTypesSuccess(listTruyenTypes: ArrayList<TruyenType>) {
        mView!!.setListTypeUI(listTruyenTypes)
    }

    override fun onLoadListTypesFailure() {
        mView!!.showViewLoadListTypesFailure()
    }
}