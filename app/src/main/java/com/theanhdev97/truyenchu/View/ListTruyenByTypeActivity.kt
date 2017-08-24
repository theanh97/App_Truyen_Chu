package com.theanhdev97.truyenchu.View

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.Slide
import android.view.MenuItem
import android.view.View
import com.kaopiz.kprogresshud.KProgressHUD
import com.theanhdev97.truyenchu.Adapter.HorizontalTruyenByTypeAdapter
import com.theanhdev97.truyenchu.Adapter.TruyenByTypeAdapter
import com.theanhdev97.truyenchu.Model.*
import com.theanhdev97.truyenchu.Model.EventBusModel.EventBusTruyenInformationReceivedData
import com.theanhdev97.truyenchu.Presenter.ListTruyenByTypePresenterImpl
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.View.Interface.ListTruyenByTypeView
import com.theanhdev97.truyenhchu.Utils.Const
import kotlinx.android.synthetic.main.activity_list_truyen_by_type.*
import com.theanhdev97.truyenchu.Utils.SimpleDividerItemDecoration
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import org.greenrobot.eventbus.EventBus


class ListTruyenByTypeActivity :
        AppCompatActivity(),
        ListTruyenByTypeView,
        TruyenByTypeAdapter.MyClickListener,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BottomNavigationView.OnNavigationItemSelectedListener, HorizontalTruyenByTypeAdapter.MyClickListener {
    override fun OnItemClickListener(url: String, position: Int, view: View) {
        mLoadingProgressDialog!!.show()
        mPresenter!!.onSelectedOneTruyen(mListTruyen!!.get(position))
    }

    // MVP
    var mPresenter: ListTruyenByTypePresenterImpl? = null
    var mListTruyenModel: ListTruyenHelper? = null
    var mTruyenInformationModel: TruyenInformationHelper? = null

    //    var mListTruyenAdapter: TruyenByTypeAdapter? = null
    var mListTruyenAdapter: HorizontalTruyenByTypeAdapter? = null
    var mListTruyen: ArrayList<Truyen>? = null
    var mLoadingProgressDialog: KProgressHUD? = null
    var mListTruyenType: TruyenType? = null
    //    var mLayoutManager: LinearLayoutManager? = null
    var mLayoutManager: GridLayoutManager? = null
    var isLoadingNewListTruyen = false
    var pageNumber = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_truyen_by_type)
//        window.attributes.windowAnimations = R.style.Fade
        initTransitionAnimation()
        mListTruyenModel = ListTruyenHelper("")
        mTruyenInformationModel = TruyenInformationHelper(this)
        mPresenter = ListTruyenByTypePresenterImpl(
                this,
                mListTruyenModel!!,
                mTruyenInformationModel!!)
        initProgressDialog()
        getAndInitListTruyenData()
        initToolbar()
        setEventListener()
    }


    override fun initTransitionAnimation() {
        val fade = Fade()
        fade.setDuration(1500)
        window.enterTransition = fade

        val slide = Slide()
        fade.setDuration(1500)
        window.returnTransition = slide
    }

    override fun getAndInitListTruyenData() {
        Const.log("get and init chapter data")
        mListTruyenType = intent.getBundleExtra(Const.HOME_PAGE_TO_LIST_TRUYENS_INTENT)
                .getSerializable(Const.LIST_TYPE) as TruyenType
        setListTruyenToUI(intent.getBundleExtra(Const.HOME_PAGE_TO_LIST_TRUYENS_INTENT)
                .getSerializable(Const.DATA_INTENT) as ArrayList<Truyen>)
    }

    override fun initToolbar() {
        setSupportActionBar(tool_bar)
        tool_bar.setNavigationIcon(R.drawable.back_icon)
        tool_bar.setNavigationOnClickListener(this)
        tv_toolbar_title.text = mListTruyenType!!.name
    }

    override fun setListTruyenToUI(listTruyen: ArrayList<Truyen>) {
        Const.log("set list truyen to ui ")
        if (mListTruyenType!!.url == Const.TRUYEN_FULL_URL)
            navigation.menu.getItem(1).setEnabled(false)
        mLoadingProgressDialog!!.dismiss()
        pageNumber = 1
        runOnUiThread {
            mListTruyen = ArrayList<Truyen>()
            mListTruyen!!.addAll(listTruyen)
            mLayoutManager = GridLayoutManager(this, 4)
            rclv_list_truyen.layoutManager = mLayoutManager
            mListTruyenAdapter = HorizontalTruyenByTypeAdapter("", this, mListTruyen!!)
            mListTruyenAdapter!!.setOnClickListener(this)
            rclv_list_truyen.adapter = mListTruyenAdapter
            swipe_refresh_layout.isRefreshing = false
        }
    }

    override fun initProgressDialog() {
        mLoadingProgressDialog = KProgressHUD.create(this)
                .setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
    }

    override fun setEventListener() {
        swipe_refresh_layout.setOnRefreshListener(this)
        rclv_list_truyen.addOnScrollListener(MyScrollListener())
        navigation.setOnNavigationItemSelectedListener(this)
    }

    override fun goToOneTruyen(truyen: Truyen) {
        runOnUiThread {
            TruyenHolder.getInstance().setCurTruyen(truyen.copy())
            mLoadingProgressDialog!!.dismiss()
            var goToInformationActivity = Intent(this, TruyenInformationActivity::class.java)
            this.startActivity(goToInformationActivity)
        }
    }

    override fun showViewError() {
        isLoadingNewListTruyen = false
        Const.toast("load truyện thất bại !!! Vui lòng kiểm tra kết nối mạng và thử lại sau",
                this)
        mLoadingProgressDialog!!.dismiss()
    }

    override fun onRefresh() {
        mPresenter!!.onSwipedToReloadListTruyen(mListTruyenType!!.url)
    }

    override fun onClick(v: View?) {
        mPresenter!!.onSelectedBack()
    }

    override fun OnItemClickListener(position: Int, view: View) {
        mLoadingProgressDialog!!.show()
        mPresenter!!.onSelectedOneTruyen(mListTruyen!!.get(position))
    }

    override fun backView() {
        this.finish()
    }

    inner class MyScrollListener() : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val totalItemCount = mLayoutManager!!.itemCount
            if (mLayoutManager!!.findLastVisibleItemPosition() == totalItemCount - 1 &&
                    isLoadingNewListTruyen == false) {
                mLoadingProgressDialog!!.show()
                isLoadingNewListTruyen = true
                mPresenter!!.onScrollToLastListTruyenPosition(
                        mListTruyenType!!.url!!,
                        pageNumber + 1)
            }
        }
    }

    override fun addMoreListTruyenToUI(listTruyen: ArrayList<Truyen>) {
        mLoadingProgressDialog!!.dismiss()
        swipe_refresh_layout.isRefreshing = false
        runOnUiThread {
            isLoadingNewListTruyen = false
            pageNumber += 1
            mListTruyen!!.addAll(listTruyen)
            mListTruyenAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mLoadingProgressDialog!!.show()
        when (item.itemId) {
            R.id.action_hot -> {
                mPresenter!!.onSelectedListTruyenType(mListTruyenType!!.url, true)
                return true
            }
            R.id.action_complete -> {
                mPresenter!!.onSelectedListTruyenType(mListTruyenType!!.url, false)
                return true
            }
            else -> return false
        }
    }
}
