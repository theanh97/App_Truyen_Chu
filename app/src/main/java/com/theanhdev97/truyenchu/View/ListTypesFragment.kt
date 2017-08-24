package com.theanhdev97.truyenchu.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kaopiz.kprogresshud.KProgressHUD
import com.theanhdev97.truyenchu.Adapter.ListTruyenTypesAdapter
import com.theanhdev97.truyenchu.Model.ListTruyenHelper
import com.theanhdev97.truyenchu.Model.ListTypesHelper
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Model.TruyenType
import com.theanhdev97.truyenchu.Presenter.ListTypesPresenterImpl
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.Utils.SimpleDividerItemDecoration
import com.theanhdev97.truyenchu.View.Interface.ListTypesView
import com.theanhdev97.truyenhchu.Utils.Const
import kotlinx.android.synthetic.main.fragment_list_types.*
import kotlinx.android.synthetic.main.fragment_list_types.view.*

/**
 * Created by DELL on 09/08/2017.
 */
class ListTypesFragment : Fragment(),
        ListTypesView, ListTruyenTypesAdapter.MyClickListener, SwipeRefreshLayout.OnRefreshListener {
    // MVP
    var mPresenter: ListTypesPresenterImpl? = null
    var mListTypesModel: ListTypesHelper? = null
    var mListTruyenModel: ListTruyenHelper? = null

    var mView: View? = null
    var mProgressDialog: KProgressHUD? = null
    var mListTruyenTypesAdapter: ListTruyenTypesAdapter? = null
    var mListTruyenTypes: ArrayList<TruyenType>? = null
    var mListTruyenType: TruyenType? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment_list_types, container, false)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListTypesModel = ListTypesHelper(Const.TRANG_CHU_URL)
        mListTruyenModel = ListTruyenHelper("")
        mPresenter = ListTypesPresenterImpl(this, mListTruyenModel!!, mListTypesModel!!)
        setEventListener()
        initProgressDialog()
    }

    override fun setListTypeUI(listTypes: ArrayList<TruyenType>) {
        mView!!.swipe_refresh_layout.isRefreshing = false
        mProgressDialog!!.dismiss()

        activity!!.runOnUiThread {
            mListTruyenTypes = ArrayList<TruyenType>()
            mListTruyenTypes!!.addAll(listTypes)
            mListTruyenTypesAdapter = ListTruyenTypesAdapter(activity, mListTruyenTypes!!)
            mListTruyenTypesAdapter!!.setOnClickListener(this)
            var li = LinearLayoutManager(activity)
            mView!!.rclv_list_types!!.layoutManager = li as RecyclerView.LayoutManager?
            mView!!.rclv_list_types!!.adapter = mListTruyenTypesAdapter
            var mDividerItemDecoration = SimpleDividerItemDecoration(activity)
            mView!!.rclv_list_types.addItemDecoration(mDividerItemDecoration)
        }
    }

    override fun initProgressDialog() {
        mProgressDialog = KProgressHUD.create(activity)
                .setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent))
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
    }

    override fun goToListTruyenView(listTruyen: ArrayList<Truyen>) {
        mProgressDialog!!.dismiss()
        var intent = Intent(activity, ListTruyenByTypeActivity::class.java)
        var bundle = Bundle()
        bundle.putSerializable(Const.DATA_INTENT, listTruyen)
        bundle.putSerializable(Const.LIST_TYPE, mListTruyenType)
        intent.putExtra(Const.HOME_PAGE_TO_LIST_TRUYENS_INTENT, bundle)
        startActivity(intent)
    }

    override fun setEventListener() {
        mView!!.swipe_refresh_layout.setOnRefreshListener(this)
    }

    override fun OnItemClickListener(position: Int, view: View) {
        mProgressDialog!!.show()
        mListTruyenType = mListTruyenTypes!![position]
        mPresenter!!.onSelectedListTruyen(mListTruyenTypes!![position].url)
    }

    override fun showViewLoadListTruyenFailure() {
        swipe_refresh_layout.isRefreshing = false
        mProgressDialog!!.dismiss()
        Const.toast("Tải danh sách truyện thất bại !!! Vui lòng kiểm tra kết nối mạng và thử " +
                "lại ", activity)
    }

    override fun onRefresh() {
        mPresenter!!.onSwipeToReloadListTypes()
    }

    override fun showViewLoadListTypesFailure() {
        swipe_refresh_layout.isRefreshing = false
        mProgressDialog!!.dismiss()
        Const.toast("Tải danh sách thể loại truyện thất bại!!! Vui lòng kiểm tra kết nối mạng" +
                "và " +
                "thử " +
                "lại ", activity)
    }
}