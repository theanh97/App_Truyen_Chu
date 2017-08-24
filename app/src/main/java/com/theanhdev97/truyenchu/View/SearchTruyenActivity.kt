package com.theanhdev97.truyenchu.View

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.theanhdev97.truyenchu.Adapter.ChaptersAdapter
import com.theanhdev97.truyenchu.Adapter.TruyenByTypeAdapter
import com.theanhdev97.truyenchu.Model.EventBusModel.EventBusTruyenInformationReceivedData
import com.theanhdev97.truyenchu.Model.SearchTruyenHelper
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Model.TruyenInformationHelper
import com.theanhdev97.truyenchu.Presenter.SearchTruyenPresenterImpl
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.Utils.SimpleDividerItemDecoration
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenchu.View.Interface.SearchTruyenView
import com.theanhdev97.truyenhchu.Utils.Const
import kotlinx.android.synthetic.main.activity_list_chapters.*
import kotlinx.android.synthetic.main.activity_search_truyen.*
import org.greenrobot.eventbus.EventBus
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.support.constraint.solver.widgets.WidgetContainer.getBounds
import android.support.v4.content.ContextCompat
import android.transition.Fade
import android.transition.Slide
import android.view.MotionEvent
import android.view.View.OnTouchListener
import com.kaopiz.kprogresshud.KProgressHUD
import com.theanhdev97.truyenchu.Model.TruyenHolder


class SearchTruyenActivity : AppCompatActivity(),
        SearchTruyenView,
        View.OnClickListener, TextWatcher, TruyenByTypeAdapter.MyClickListener {

    // MVP
    var mPresenter: SearchTruyenPresenterImpl? = null
    var mSearchTruyenModel: SearchTruyenHelper? = null
    var mTruyenInformationModel: TruyenInformationHelper? = null

    var mListTruyens: ArrayList<Truyen>? = null
    var mListTruyensAdapter: TruyenByTypeAdapter? = null
    var mLayoutManager: LinearLayoutManager? = null
    var mProgressDialog: KProgressHUD? = null
    var mIsLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_truyen)
//        window.attributes.windowAnimations = R.style.translate
        initTransitionAnimation()
        initToolbar()
        initProgressDialog()
        repairUI()
        setEventListener()
        mSearchTruyenModel = SearchTruyenHelper()
        mTruyenInformationModel = TruyenInformationHelper(this)
        mPresenter = SearchTruyenPresenterImpl(this, mSearchTruyenModel!!, mTruyenInformationModel!!)
    }

    override fun initTransitionAnimation() {
        val fade = Fade()
        fade.setDuration(1500)
        window.enterTransition = fade

        val slide = Slide()
        fade.setDuration(1500)
        window.returnTransition = slide
    }

    override fun initProgressDialog() {
        mProgressDialog = KProgressHUD.create(this)
                .setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
    }

    override fun initToolbar() {
        setSupportActionBar(tool_bar)
        tool_bar.setNavigationIcon(R.drawable.back_icon)
        tool_bar.setNavigationOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun setEmptyListTruyensToUI() {
        mListTruyens!!.clear()
        mListTruyensAdapter!!.notifyDataSetChanged()
    }

    override fun setEventListener() {
        edt_search_truyen.addTextChangedListener(this)
        mListTruyensAdapter!!.setOnClickListener(this)
        rclv_list_truyens_search.addOnScrollListener(MyScrollListener())

        edt_search_truyen.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= edt_search_truyen.getRight() - edt_search_truyen.getCompoundDrawables()
                        [DRAWABLE_RIGHT].getBounds().width()) {
                    // your action here
                    mPresenter!!.onSeletedClearKeyWords()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    override fun removeSearchKeyWords() {
        edt_search_truyen.setText("")
    }

    override fun onClick(v: View?) {
        mPresenter!!.onSelectedBackNavigation()
    }

    override fun backToHome() {
        this.finish()
    }

    override fun goToOneTruyenView(truyen: Truyen) {
        runOnUiThread {
            TruyenHolder.getInstance().setCurTruyen(truyen.copy())
            mProgressDialog!!.dismiss()
            var goToInformationActivity = Intent(this, TruyenInformationActivity::class.java)
            this.startActivity(goToInformationActivity)
        }

//        runOnUiThread {
//
//
//            //            TruyenUtils.updateTruyenFromInternalStorage(truyen, this)
//            var goToInformationActivity = Intent(this, TruyenInformationActivity::class.java)
//            this.startActivity(goToInformationActivity)
//        }
//        Handler().postDelayed({
//            EventBus.getDefault().post(EventBusTruyenInformationReceivedData(truyen))
//            mProgressDialog!!.dismiss()
//        }, 30)
    }

    override fun repairUI() {
        runOnUiThread {
            mListTruyens = ArrayList<Truyen>()
            mListTruyensAdapter = TruyenByTypeAdapter(this, mListTruyens!!)
            mLayoutManager = LinearLayoutManager(this)
            rclv_list_truyens_search!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
            rclv_list_truyens_search!!.adapter = mListTruyensAdapter
            var mDividerItemDecoration = SimpleDividerItemDecoration(this)
            rclv_list_truyens_search.addItemDecoration(mDividerItemDecoration)
        }
    }

    override fun setListTruyenToUI(listTruyen: ArrayList<Truyen>) {
        runOnUiThread {
            mListTruyens!!.clear()
            mListTruyens!!.addAll(listTruyen)
            mListTruyensAdapter!!.notifyDataSetChanged()
        }
    }

    override fun OnItemClickListener(position: Int, view: View) {
        hideVirtualKeyboard()
        mProgressDialog!!.show()
        mPresenter!!.onSelectedTruyen(mListTruyens!![position])
    }

    override fun showViewSearchTruyenError() {
        mProgressDialog!!.dismiss()

//        Const.toast("Tìm kiếm thất bại !!! vui lòng kiểm tra kết nối mạng và thử lại sau", this)
    }

    override fun hideVirtualKeyboard() {
        var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edt_search_truyen.windowToken, 0)
    }

    override fun showViewLoadTruyenError() {
        mProgressDialog!!.dismiss()
        Const.toast("Tải truyện thất bại !!! vui lòng kiểm tra kết nối mạng và thử lại sau", this)
    }


    override fun addMoreListTruyenToUI(listTruyens: ArrayList<Truyen>) {
        mProgressDialog!!.dismiss()
        mIsLoading = false
        mListTruyens!!.addAll(listTruyens)
        mListTruyensAdapter!!.notifyDataSetChanged()
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Const.log("Text Changed : ${s.toString()}")
        mPresenter!!.onSearchKeyWordsChanged(s.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_search) {
            var keyWords = edt_search_truyen.text.toString()
            mPresenter!!.onClickSearch(keyWords)
        }
        return true
    }

    inner class MyScrollListener() : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            var totalItemCount = mLayoutManager!!.itemCount
            if (mLayoutManager!!.findLastVisibleItemPosition() == totalItemCount - 1
                    && mListTruyens!!.size > 10
                    && mIsLoading == false
                    && mPresenter!!.checkLimitSearchPages() == false) {
                mProgressDialog!!.show()
                mIsLoading = true
                mPresenter!!.onSwipeToLoadMoreTruyens()
            }
        }
    }

}
