package com.theanhdev97.truyenchu.View

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.kaopiz.kprogresshud.KProgressHUD
import com.theanhdev97.truyenchu.Adapter.HorizontalTruyenByTypeAdapter
import com.theanhdev97.truyenchu.Model.*
import com.theanhdev97.truyenchu.Presenter.HomePagePresenterImpl
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenchu.View.Interface.HomePageView
import com.theanhdev97.truyenhchu.Utils.Const
import kotlinx.android.synthetic.main.fragment_home_page.view.*
import com.theanhdev97.truyenchu.Model.EventBusModel.EventBusTruyenInformationReceivedData
import kotlinx.android.synthetic.main.fragment_home_page.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by DELL on 09/08/2017.
 */
class HomePageFragment : Fragment(),
        HomePageView,
        HorizontalTruyenByTypeAdapter.MyClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {


    // MVP
    var mPresenter: HomePagePresenterImpl? = null
    var mHomePageModel: HomePageHelper? = null
    var mTruyenInformationModel: TruyenInformationHelper? = null
    var mListTruyenModel: ListTruyenHelper? = null

    var mView: View? = null
    var mListTruyenHot: ArrayList<Truyen>? = null
    var mListTruyenHotAdapter: HorizontalTruyenByTypeAdapter? = null
    var mListTruyenHoanThanh: ArrayList<Truyen>? = null
    var mListTruyenHoanThanhAdapter: HorizontalTruyenByTypeAdapter? = null
    var mListTruyenMoi: ArrayList<Truyen>? = null
    var mLoadingHomePageProgress: KProgressHUD? = null
    var mListTruyenMoiAdapter: HorizontalTruyenByTypeAdapter? = null
    var mSelectedListTruyenType: TruyenType? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment_home_page, container, false)
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView!!.swipe_refresh_layout.isRefreshing = true

        setProgressDialog()
            setEventListener()
            repairUI()
            mHomePageModel = HomePageHelper()
            mTruyenInformationModel = TruyenInformationHelper(activity)
            mListTruyenModel = ListTruyenHelper("")
            mPresenter = HomePagePresenterImpl(
                    this,
                    mHomePageModel!!,
                    mTruyenInformationModel!!,
                    mListTruyenModel!!)
    }

    override fun repairUI() {
        activity!!.runOnUiThread {

            var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            var layoutManager1 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            var layoutManager2 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);


            // Truyen hot
            mListTruyenHot = ArrayList<Truyen>()
            mListTruyenHotAdapter = HorizontalTruyenByTypeAdapter(
                    Const.TRUYEN_HOT_URL,
                    context,
                    mListTruyenHot!!)
            mView!!.rclv_list_truyen_hot.layoutManager = layoutManager as RecyclerView.LayoutManager?
            mListTruyenHotAdapter!!.setOnClickListener(this)
            mView!!.rclv_list_truyen_hot.adapter = mListTruyenHotAdapter

            // Truyen Full
            mListTruyenHoanThanh = ArrayList<Truyen>()
            mListTruyenHoanThanhAdapter = HorizontalTruyenByTypeAdapter(Const.TRUYEN_FULL_URL,
                    context,
                    mListTruyenHoanThanh!!)
            mView!!.rclv_list_truyen_hoan_thanh.layoutManager = layoutManager1 as RecyclerView.LayoutManager?
            mListTruyenHoanThanhAdapter!!.setOnClickListener(this)
            mView!!.rclv_list_truyen_hoan_thanh.adapter = mListTruyenHoanThanhAdapter

            // Truyen Moi
            mListTruyenMoi = ArrayList<Truyen>()
            mListTruyenMoiAdapter = HorizontalTruyenByTypeAdapter(Const.TRUYEN_MOI_URL,
                    context,
                    mListTruyenMoi!!)
            mView!!.rclv_list_truyen_moi.layoutManager = layoutManager2 as RecyclerView.LayoutManager?
            mListTruyenMoiAdapter!!.setOnClickListener(this)
            mView!!.rclv_list_truyen_moi.adapter = mListTruyenMoiAdapter

//            Handler().postDelayed({
//                mView!!.tv_truyenhot_title
//                        .setBackgroundColor(ContextCompat.getColor(activity, R.color.list_chapters_toolbar_toolbar_background))
//                mView!!.tv_truyenhot_title
//                        .text = "Truyện hot"
//            }, 150)

        }
    }

    override fun setListTruyenToView(listTruyenHot: ArrayList<Truyen>,
                                     listTruyenHoanThanh: ArrayList<Truyen>,
                                     listTruyenMoi: ArrayList<Truyen>) {
        activity.runOnUiThread {
            // Truyen Hot
            mView!!.tv_truyenhot_title.text = "Truyện hot"
            mView!!.btn_view_all_truyen_hot.text = "Xem Thêm"
            mListTruyenHot!!.clear()
            mListTruyenHot!!.addAll(listTruyenHot)
            mListTruyenHotAdapter!!.notifyDataSetChanged()

            // Truyen Hoan Thanh
            mView!!.tv_truyen_hoan_thanh_title.text = "Truyện hoàn thành"
            mView!!.btn_view_all_truyen_hoan_thanh.text = "Xem Thêm"
            mListTruyenHoanThanh!!.clear()
            mListTruyenHoanThanh!!.addAll(listTruyenHoanThanh)
            mListTruyenHoanThanhAdapter!!.notifyDataSetChanged()

            // Truyen Moi
            mView!!.tv_truyen_moi_title.text = "Truyện mới"
            mView!!.btn_view_all_truyen_moi.text = "Xem Thêm"
            mListTruyenMoi!!.clear()
            mListTruyenMoi!!.addAll(listTruyenMoi)
            mListTruyenMoiAdapter!!.notifyDataSetChanged()

            mView!!.swipe_refresh_layout!!.isRefreshing = false
        }
        mLoadingHomePageProgress!!.dismiss()
    }

    override fun showViewHomePageError() {
        mLoadingHomePageProgress!!.dismiss()
        Const.toast("Tải Trang chủ thất bại !!! Vui lòng kiểm tra nối mạng và thử lại", context)
    }


    override fun setEventListener() {
        mView!!.swipe_refresh_layout.setOnRefreshListener(this)

        mView!!.btn_view_all_truyen_hot.setOnClickListener(this)
        mView!!.btn_view_all_truyen_moi.setOnClickListener(this)
        mView!!.btn_view_all_truyen_hoan_thanh.setOnClickListener(this)
    }

    override fun setProgressDialog() {
        mLoadingHomePageProgress = KProgressHUD.create(activity)
                .setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent))
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
    }

    override fun goToOneTruyen(truyen: Truyen) {
        activity.runOnUiThread {
            TruyenHolder.getInstance().setCurTruyen(truyen.copy())
            mLoadingHomePageProgress!!.dismiss()
            var goToInformationActivity = Intent(activity, TruyenInformationActivity::class.java)
            this.startActivity(goToInformationActivity)
        }
//        activity.runOnUiThread {
////            TruyenUtils.updateTruyenFromInternalStorage(truyen, activity)
//        }
//        Handler().postDelayed({
//            EventBus.getDefault().post(EventBusTruyenInformationReceivedData(truyen))
//        }, 50)
    }

    override fun goToListTruyensView(listTruyens: ArrayList<Truyen>) {
        mLoadingHomePageProgress!!.dismiss()
        var intent = Intent(activity, ListTruyenByTypeActivity::class.java)
        var bundle = Bundle()
        bundle.putSerializable(Const.DATA_INTENT, listTruyens)
        bundle!!.putSerializable(
                Const.LIST_TYPE, mSelectedListTruyenType)
        intent!!.putExtra(Const.HOME_PAGE_TO_LIST_TRUYENS_INTENT, bundle)
        startActivity(intent)
    }

    override fun onRefresh() {
        mPresenter!!.onRefreshData()
    }

    override fun OnItemClickListener(url: String, position: Int, view: View) {
        mLoadingHomePageProgress!!.show()
        when (url) {
            Const.TRUYEN_HOT_URL -> {
                mPresenter!!.onSelectedTruyen(mListTruyenHot!![position].copy())
            }
            Const.TRUYEN_FULL_URL -> {
                mPresenter!!.onSelectedTruyen(mListTruyenHoanThanh!![position].copy())
            }
            Const.TRUYEN_MOI_URL -> {
                mPresenter!!.onSelectedTruyen(mListTruyenMoi!![position].copy())
            }
            else -> {
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_view_all_truyen_hot -> {
                mSelectedListTruyenType = TruyenType("Truyện Hot", Const.TRUYEN_HOT_URL)
                mPresenter!!.onSelectedListTruyenByType(Const.TRUYEN_HOT_URL)
            }
            R.id.btn_view_all_truyen_moi -> {
                mSelectedListTruyenType = TruyenType("Truyện Mới", Const.TRUYEN_MOI_URL)
                mPresenter!!.onSelectedListTruyenByType(Const.TRUYEN_MOI_URL)
            }
            R.id.btn_view_all_truyen_hoan_thanh -> {
                mSelectedListTruyenType = TruyenType("Truyện Hoàn Thành", Const.TRUYEN_FULL_URL)
                mPresenter!!.onSelectedListTruyenByType(Const.TRUYEN_FULL_URL)
            }
            else -> {
            }
        }
    }


    override fun showViewLoadTruyenError() {
        mView!!.swipe_refresh_layout.isRefreshing = false
        mLoadingHomePageProgress!!.dismiss()
        Const.toast("Tải truyện thất bại !!! Vui lòng kiếm tra kết nối mạng và thử lại", context)
    }

    override fun showViewLoadListTruyenError() {
        mView!!.swipe_refresh_layout.isRefreshing = false
        mLoadingHomePageProgress!!.dismiss()
        Const.toast("tải danh sách truyện thất bại !!! Vui lòng kiểm tra nối mạng và tải " +
                "lại", context)
    }
}