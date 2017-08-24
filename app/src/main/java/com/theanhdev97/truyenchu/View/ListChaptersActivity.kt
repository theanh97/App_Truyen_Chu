package com.theanhdev97.truyenchu.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.Slide
import android.view.View
import com.theanhdev97.truyenchu.Adapter.ChaptersAdapter
import com.theanhdev97.truyenchu.Model.Chapter
import com.theanhdev97.truyenchu.Model.ChapterHelper
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Presenter.ListChaptersPresenterImpl
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.Utils.SimpleDividerItemDecoration
import com.theanhdev97.truyenchu.View.Interface.ListChaptersView
import com.theanhdev97.truyenhchu.Utils.Const
import kotlinx.android.synthetic.main.activity_list_chapters.*
import android.view.MenuItem
import com.kaopiz.kprogresshud.KProgressHUD
import com.theanhdev97.truyenchu.Model.EventBusModel.EventBusListChapterReceivedData
import com.theanhdev97.truyenchu.Model.TruyenHolder
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ListChaptersActivity : AppCompatActivity(),
        ListChaptersView, ChaptersAdapter.MyClickListener, View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {


    // MVP
    var mPresenter: ListChaptersPresenterImpl? = null
    var mModel: ChapterHelper? = null

    var mTruyen: Truyen? = null
    var mListChaptersAdapters: ChaptersAdapter? = null
    var mLayoutManager: LinearLayoutManager? = null
    var mSelectedChapterPosition: Int = -1
    var mProgressDialog: KProgressHUD? = null
    var mIsDescreasing = true

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_chapters)
//        window.attributes.windowAnimations = R.style.Fade
        initTransitionAnimation()
        initToolbar()
        setEventListener()
        initProgressDialog()
        getData()
        setListChaptersToUI()
        mModel = ChapterHelper()
        mPresenter = ListChaptersPresenterImpl(this, mModel!!)
    }

    override fun initTransitionAnimation() {
        val fade = Fade()
        fade.setDuration(1500)
        window.enterTransition = fade

        val slide = Slide()
        fade.setDuration(1500)
        window.returnTransition = slide
    }

    override fun getData() {
        mTruyen = TruyenHolder.getInstance().getCurTruyen()
    }

    override fun setEventListener() {
        nav.setOnNavigationItemSelectedListener(this)
    }

    override fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar!!.setNavigationIcon(R.drawable.back_icon)
        toolbar!!.setNavigationOnClickListener(this)
    }

    override fun initProgressDialog() {
        mProgressDialog = KProgressHUD.create(this)
                .setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
    }

    override fun onClick(v: View?) {
        mPresenter!!.onSelectedBackNavigation()
    }

    override fun backToTruyenInformation() {
        finish()
    }

    override fun setListChaptersToUI() {
        runOnUiThread {
            mListChaptersAdapters = ChaptersAdapter(this, mTruyen!!.listChapter!!)
            mListChaptersAdapters!!.setOnClickListener(this)
            mLayoutManager = LinearLayoutManager(this)
            mLayoutManager!!.reverseLayout = true
            mLayoutManager!!.setStackFromEnd(true);
            rclv_list_chapters!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
            rclv_list_chapters!!.adapter = mListChaptersAdapters
            var mDividerItemDecoration = SimpleDividerItemDecoration(this)
            rclv_list_chapters.addItemDecoration(mDividerItemDecoration)
            rclv_list_chapters.isVerticalScrollBarEnabled = true
//            fastscroll.setRecyclerView(rclv_list_chapters)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> {
                mPresenter!!.onSelectedSortMenu(item, mIsDescreasing)
                return true
            }
            R.id.action_read_continous -> {
                mProgressDialog!!.show()
                mSelectedChapterPosition = TruyenUtils.getContinousChapterPosition(
                        this,
                        mTruyen!!.title)
                mPresenter!!.onSelectedContinousChapter(mTruyen!!.listChapter!![mSelectedChapterPosition])
                return true
            }
            else -> return false
        }
    }

    override fun showDescreaseView(menu: MenuItem) {
        // recyler view
        mLayoutManager!!.reverseLayout = true
        mLayoutManager!!.setStackFromEnd(true);
        rclv_list_chapters.layoutManager = mLayoutManager

        // menu
        menu.setTitle("Giảm dần")
                .setIcon(R.drawable.arrow_down)
        mIsDescreasing = true
    }

    override fun showInscreaseView(menu: MenuItem) {
        // recyler view
        mLayoutManager!!.reverseLayout = false
        mLayoutManager!!.setStackFromEnd(false);
        rclv_list_chapters.layoutManager = mLayoutManager

        // menu
        menu.setTitle("Tăng dần")
                .setIcon(R.drawable.arrow_up)
        mIsDescreasing = false
    }

    override fun goToSelectedChapter(chapter: Chapter) {
        mProgressDialog!!.dismiss()
        mTruyen!!.listChapter!![mSelectedChapterPosition] = chapter

        // open chapter Activity && send data
        var intent = Intent(this@ListChaptersActivity, ChapterActivity::class.java)
        intent.putExtra(Const.CURRENT_CHAPTER_POSITION_INTENT, mSelectedChapterPosition)
        startActivity(intent)


    }

    override fun OnItemClickListener(position: Int, view: View) {
        mProgressDialog!!.show()
        mSelectedChapterPosition = position
        mPresenter!!.onSelectedChapter(mTruyen!!.listChapter!![position])
    }

    override fun showViewError() {
        mProgressDialog!!.dismiss()
        Const.toast("Tải Chương thất bại !!! Vui lòng kiểm tra kết nối mạng và thử lại ", this)
    }
}
