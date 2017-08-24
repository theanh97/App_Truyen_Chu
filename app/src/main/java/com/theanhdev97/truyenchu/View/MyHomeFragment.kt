package com.theanhdev97.truyenchu.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kaopiz.kprogresshud.KProgressHUD
import com.theanhdev97.truyenchu.Adapter.HorizontalTruyenByTypeAdapter
import com.theanhdev97.truyenchu.Model.*
import com.theanhdev97.truyenchu.Model.EventBusModel.EventBusTruyenInformationReceivedData
import com.theanhdev97.truyenchu.Presenter.MyHomePresenterImpl
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenchu.View.Interface.MyHomeView
import com.theanhdev97.truyenhchu.Utils.Const
import kotlinx.android.synthetic.main.fragment_my_home.*
import kotlinx.android.synthetic.main.fragment_my_home.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by DELL on 09/08/2017.
 */
class MyHomeFragment : Fragment(),
        SwipeRefreshLayout.OnRefreshListener,
        MyHomeView,
        HorizontalTruyenByTypeAdapter.MyClickListener/*, View.OnClickListener */ {
    // MVP
    var mPresenter: MyHomePresenterImpl? = null
    var mModel: TruyenInformationHelper? = null

    var mView: View? = null
    var mProgressDialog: KProgressHUD? = null
    var mReadingLayoutManager: LinearLayoutManager? = null
    var mDownLoadLayoutManager: LinearLayoutManager? = null
    var mListTruyenReadingAdapter: HorizontalTruyenByTypeAdapter? = null
    var mListTruyenReading: ArrayList<Truyen>? = null
    var mListTryuenDownLoadedAdapter: HorizontalTruyenByTypeAdapter? = null
    var mListTruyenDownLoaded: ArrayList<Truyen>? = null
    var mSelectedListTruyenType: TruyenType? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment_my_home, container, false)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        repairUI()
        setEventListener()
        initProgressDialog()
        mModel = TruyenInformationHelper(activity)
        mPresenter = MyHomePresenterImpl(this, mModel!!)
    }

    override fun getData() {
        mListTruyenReading = ArrayList<Truyen>()
        val list = getListTruyenReading()
        if (list.size > 0)
            mListTruyenReading!!.addAll(getListTruyenReading())

        mListTruyenDownLoaded = ArrayList<Truyen>()
        val list2 = getListTruyenDownLoad()
        if (list2.size > 0)
            mListTruyenDownLoaded!!.addAll(getListTruyenDownLoad())
    }

    override fun initProgressDialog() {
        mProgressDialog = KProgressHUD.create(activity)
                .setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent))
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
    }

    override fun setEventListener() {
        mView!!.swipe_refresh_layout_my_home.setOnRefreshListener(this)

//        mView!!.btn_view_all_truyen_downloaded.setOnClickListener(this)
//        mView!!.btn_view_all_truyen_reading.setOnClickListener(this)
    }

    override fun getListTruyenDownLoad(): ArrayList<Truyen> {
        return TruyenHolder.getInstance().getListDownloaded()
    }

    override fun getListTruyenReading(): ArrayList<Truyen> {
        return TruyenHolder.getInstance().getListReading()
    }

    override fun showViewLoadTruyenError() {
        mProgressDialog!!.dismiss()
        Const.toast("Tải truyện thất bại !!! Vui lòng kiểm tra kết nối mạng và thử lại sau", activity)
    }

    override fun updateListTruyenToUI() {
        activity.runOnUiThread {
            val list = getListTruyenReading()
            if (list.size > 0) {
                mListTruyenReading!!.clear()
                mListTruyenReading!!.addAll(getListTruyenReading())
            }
            mListTruyenReadingAdapter!!.notifyDataSetChanged()

            val list2 = getListTruyenDownLoad()
            if (list2.size > 0) {
                mListTruyenDownLoaded!!.clear()
                mListTruyenDownLoaded!!.addAll(getListTruyenDownLoad())
            }
            mListTryuenDownLoadedAdapter!!.notifyDataSetChanged()

            mView!!.swipe_refresh_layout_my_home.isRefreshing = false
        }
    }

    override fun repairUI() {
        // list truyen reading
        mReadingLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mDownLoadLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        mListTruyenReadingAdapter = HorizontalTruyenByTypeAdapter(
                Const.TRUYEN_READING_URL,
                activity,
                mListTruyenReading!!)
        mListTruyenReadingAdapter!!.setOnClickListener(this)
        mView!!.rclv_list_truyen_reading.layoutManager = mReadingLayoutManager
        mView!!.rclv_list_truyen_reading.adapter = mListTruyenReadingAdapter

        // list truyen downloaded
        mListTryuenDownLoadedAdapter = HorizontalTruyenByTypeAdapter(
                Const.TRUYEN_DOWNLOAD_URL,
                activity,
                mListTruyenDownLoaded!!)
        mListTryuenDownLoadedAdapter!!.setOnClickListener(this)
        mView!!.rclv_list_truyen_downloaded.layoutManager = mDownLoadLayoutManager
        mView!!.rclv_list_truyen_downloaded.adapter = mListTryuenDownLoadedAdapter

        mView!!.tv_truyen_reading_title.text = "Truyện đang đọc"
        mView!!.tv_truyen_downloaded_title.text = "Truyện đã tải"
    }

    override fun onRefresh() {
        mView!!.swipe_refresh_layout_my_home.isRefreshing = true
        mPresenter!!.onSwipeToRefreshListTruyen()
    }

//    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.btn_view_all_truyen_reading -> {
//                mSelectedListTruyenType = TruyenType("Truyện Đang đọc", Const.TRUYEN_READING_URL)
//                mPresenter!!.onSelectedListTruyenByType()
//            }
//            R.id.btn_view_all_truyen_downloaded -> {
//                mSelectedListTruyenType = TruyenType("Truyện đã tải", Const.TRUYEN_DOWNLOAD_URL)
//                mPresenter!!.onSelectedListTruyenByType()
//            }
//            else -> {
//            }
//        }
//    }

    override fun OnItemClickListener(url: String, position: Int, view: View) {
        Handler().post({
            mProgressDialog!!.show()
        })
        activity.runOnUiThread {
            // reading item
            if (url.equals(Const.TRUYEN_READING_URL))
                mPresenter!!.onSelectedTruyen(mListTruyenReading!![position])
            // download item
            else {
                mPresenter!!.onSelectedTruyen(mListTruyenDownLoaded!![position].copy())
            }
        }
    }

    override fun goToOneTruyenView(truyen: Truyen) {
        activity.runOnUiThread {
            TruyenHolder.getInstance().setCurTruyen(truyen.copy())
            mProgressDialog!!.dismiss()
            var goToInformationActivity = Intent(activity, TruyenInformationActivity::class.java)
            this.startActivity(goToInformationActivity)
        }
    }


//    override fun goToListTruyenView() {
//        mProgressDialog!!.dismiss()
//        var intent = Intent(activity, ListTruyenByTypeActivity::class.java)
//        var bundle = Bundle()
//        bundle!!.putSerializable(
//                Const.LIST_TYPE, mSelectedListTruyenType!!.copy())
//        intent!!.putExtra(Const.HOME_PAGE_TO_LIST_TRUYENS_INTENT, bundle)
//        startActivity(intent)
//}
}