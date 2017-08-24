package com.theanhdev97.truyenchu.Presenter

import com.theanhdev97.truyenchu.Model.Callback.OnLoadTruyenInformationResult
import com.theanhdev97.truyenchu.Model.Callback.OnSearchTruyenResult
import com.theanhdev97.truyenchu.Model.SearchTruyenHelper
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Model.TruyenInformationHelper
import com.theanhdev97.truyenchu.Presenter.Interface.SearchTruyenPresenter
import com.theanhdev97.truyenchu.View.Interface.SearchTruyenView

/**
 * Created by DELL on 13/08/2017.
 */
class SearchTruyenPresenterImpl(view: SearchTruyenView,
                                searchTruyenModel: SearchTruyenHelper,
                                truyenInformationModel: TruyenInformationHelper)
    : SearchTruyenPresenter,
        OnSearchTruyenResult, OnLoadTruyenInformationResult {


    // MVP
    var mView: SearchTruyenView? = null
    var mSearchTruyenModel: SearchTruyenHelper? = null
    var mTruyenInformationModel: TruyenInformationHelper? = null

    var mMaxPageSearchSize = 1
    var mCurrentPage = 1
    var isLoadMore = false

    init {
        this.mView = view
        this.mSearchTruyenModel = searchTruyenModel
        this.mSearchTruyenModel!!.setOnSearchTruyenListener(this)

        this.mTruyenInformationModel = truyenInformationModel
        this.mTruyenInformationModel!!.setOnLoadTruyenResult(this)
    }

    override fun onSelectedBackNavigation() {
        mView!!.backToHome()
    }

    override fun onSearchTruyenSuccess(listTruyen: ArrayList<Truyen>, maxPageSearchSize: Int) {
        if (isLoadMore == false) {
            mCurrentPage = 1
            mView!!.setListTruyenToUI(listTruyen)
            mMaxPageSearchSize = maxPageSearchSize
        } else {
            mCurrentPage += 1
            mView!!.addMoreListTruyenToUI(listTruyen)
            isLoadMore = false
        }
    }

    override fun onSearchTruyenFailure() {
        mView!!.showViewSearchTruyenError()
    }

    override fun onSearchKeyWordsChanged(keyWord: String) {
        if (keyWord.length > 2)
            getListTruyenByKeyWords(keyWord)
        else
            mView!!.setEmptyListTruyensToUI()
    }

    override fun onSwipeToLoadMoreTruyens() {
        if (mCurrentPage + 1 <= mMaxPageSearchSize) {
            isLoadMore = true
            mSearchTruyenModel!!.getListTruyensByPostion(mCurrentPage + 1)
        }
    }

    override fun checkLimitSearchPages(): Boolean {
        if (mCurrentPage == mMaxPageSearchSize)
            return true
        return false
    }

    override fun getListTruyenByKeyWords(keyWords: String) {
        mSearchTruyenModel!!.setKeyWords(keyWords)
        mSearchTruyenModel!!.getListTruyens()
    }

    override fun onClickSearch(keyWords: String) {
        getListTruyenByKeyWords(keyWords)
    }

    override fun loadTruyen(truyen: Truyen) {
        mTruyenInformationModel!!.setTruyen(truyen)
        mTruyenInformationModel!!.getTruyen()
    }

    override fun onSeletedClearKeyWords() {
        mView!!.removeSearchKeyWords()
    }

    override fun onSelectedTruyen(truyen: Truyen) {
        loadTruyen(truyen)
    }

    override fun onLoadTruyenInformationSuccess(truyen: Truyen) {
        mView!!.goToOneTruyenView(truyen)
    }

    override fun onLoadTruyenInformationFailure() {
       mView!!.showViewLoadTruyenError()
    }
}