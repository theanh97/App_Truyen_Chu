package com.theanhdev97.truyenchu.View

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.kaopiz.kprogresshud.KProgressHUD
import com.theanhdev97.truyenchu.Model.*
import com.theanhdev97.truyenchu.Model.EventBusModel.EventBusListChapterReceivedData
import com.theanhdev97.truyenchu.Model.EventBusModel.EventBusTruyenInformationReceivedData
import com.theanhdev97.truyenchu.Presenter.TruyenInformationPresenterImpl
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenchu.View.Interface.TruyenInformationView
import com.theanhdev97.truyenhchu.Utils.Const
import kotlinx.android.synthetic.main.activity_list_truyen_by_type.*
import kotlinx.android.synthetic.main.activity_truyen_information.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.PorterDuff
import android.transition.Fade
import android.transition.Slide


class TruyenInformationActivity : AppCompatActivity()
        , TruyenInformationView, BottomNavigationView.OnNavigationItemSelectedListener {

    // MVP
    var mPresenter: TruyenInformationPresenterImpl? = null
    var mListChapterModel: ListChaptersHelper? = null
    var mDownLoadTruyenModel: DownLoadTruyenHelper? = null
    var mChapterModel: ChapterHelper? = null

    var mTruyen: Truyen? = null
    //    var mDownLoadTruyenProgressDialog: ProgressDialog? = null
    var mProgressDialog: KProgressHUD? = null
    var mDownLoadProgressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truyen_information)
        initTransitionAnimation()
        getTruyen()
        initToolbar()
        setTruyenToUI(mTruyen!!)
        initProgressDialog()
        registerEvent()
        mListChapterModel = ListChaptersHelper(mTruyen!!)
        mDownLoadTruyenModel = DownLoadTruyenHelper(this)
        mChapterModel = ChapterHelper()
        mPresenter = TruyenInformationPresenterImpl(
                this,
                mListChapterModel!!,
                mDownLoadTruyenModel!!,
                mChapterModel!!)

    }

    override fun initTransitionAnimation() {
        val fade = Fade()
        fade.setDuration(1500)
        window.enterTransition = fade

        val slide = Slide()
        fade.setDuration(1500)
        window.returnTransition = slide
    }

    override fun initToolbar() {
        setSupportActionBar(tool_bar_truyen_information)
        tool_bar_truyen_information.setNavigationIcon(R.drawable.back_icon)
        tool_bar_truyen_information.setNavigationOnClickListener {
            mPresenter!!.onBackListTruyenByTypeView()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_download -> {
                mPresenter!!.onSelectedDownLoadTruyen()
            }
            R.id.action_list_chapters -> {
                mPresenter!!.onShowListChaptersView(mTruyen!!.copy())
            }
            R.id.action_read_continous -> {
                mProgressDialog!!.show()
                val chapterPosition = TruyenUtils.getContinousChapterPosition(this, mTruyen!!.title)
                mPresenter!!.onReadContinousChapter(
                        mTruyen!!.listChapter!![chapterPosition].copy(),
                        chapterPosition)
            }
        }
        return true
    }

    override fun showDownLoadDialog() {
        mDownLoadProgressDialog!!.show()
    }

    override fun backToListTruyenByTypeView() {
        finish()
    }

    override fun setTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.attributes.windowAnimations = R.style.Fade
    }

    override fun initProgressDialog() {
        // loading
        mProgressDialog = KProgressHUD.create(this)
                .setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)

        // download
        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        builder.setTitle("")
        builder.setMessage("Bạn có chắc tải truyện không ?")

        val positiveText = "Có"
        builder.setPositiveButton(positiveText
        ) { dialog, which ->
            mPresenter!!.onDownLoadTruyen(mTruyen!!.copy())
        }

        val negativeText = "Không"
        builder.setNegativeButton(negativeText
        ) { dialog, which ->
            // negative button logic
        }

        mDownLoadProgressDialog = builder.create()
    }

    override fun getTruyen() {
        mTruyen = TruyenHolder.getInstance().getCurTruyen()
    }

    override fun registerEvent() {
        navigation_truyen_information.setOnNavigationItemSelectedListener(this)
    }

    override fun showLoadListChapterViewError() {
        mProgressDialog!!.dismiss()
        Const.toast("Load danh sách chương thất bại !!! Vui lòng kiểm tra kết nối mạng và tải" +
                " lại",
                this)
    }

    override fun showDownLoadTruyenError() {
        Const.toast("tải Truyện thất bại !!! Vui lòng kiểm tra kết nối mạng và tải" +
                " lại", this)
    }

    override fun showDownLoadTruyenSuccess() {
        // can't click download again
        navigation_truyen_information.menu.getItem(2).setEnabled(false)
        navigation_truyen_information.menu.getItem(2).title = "Đã tải truyện"

        Const.toast("Tải truyện hoàn thành", this)
    }

    override fun setTruyenToUI(truyen: Truyen) {
        mTruyen = truyen
        runOnUiThread {
            //             IMAGE
            Glide.with(this)
                    .load(truyen.imageTwo)
                    .placeholder(R.drawable.book_replace)
                    .into(imv_image_truyen_information)

            // TOOLBAR TITLE
            tv_toolbar_title_truyen_information.text = truyen.title

            // TITLE
            tv_title_truyen_information.text = truyen.title

            // AUTHOR
            tv_author_truyen_information.text = "Tác giả : ${truyen.author}"

            // MAX CHAPTER
            tv_max_chapter_truyen_information.text = "Số chương : ${truyen.listChapter!!.size}"

            // RATING
            var rating = SpannableString("Điểm : ${mTruyen!!.rate}/10.0")
            rating.setSpan(ForegroundColorSpan(Color.parseColor("#2196F3")), 7, rating.length - 5, 0);
            tv_rating_truyen_information.text = rating

            // STATUS
            tv_status_truyen_information.text = "Tình Trạng : ${truyen.status}"

            // DESCRIPTION
            expandable_description.setText(truyen!!.description)
            val face = Typeface.createFromAsset(assets, "pala.ttf")
            expandable_text.setTypeface(face, Typeface.NORMAL)

            // set download cannot able if downloaded
            if (!TruyenUtils.isDownloadable(mTruyen!!, mTruyen!!.listChapter!!.size)) {
                navigation_truyen_information.menu.getItem(2).title = "Đã tải truyện"
                navigation_truyen_information.menu.getItem(2).setEnabled(false)
            }
        }

    }

    override fun goToListChaptersView(truyen: Truyen) {
        val intent = Intent(this, ListChaptersActivity::class.java)
        startActivity(intent)
    }

    override fun goToContinousChapterView(chapter: Chapter) {
        runOnUiThread {
            mTruyen!!.listChapter!![chapter.positionTag] = chapter.copy()
            TruyenHolder.getInstance().setCurTruyen(mTruyen!!.copy())
            mProgressDialog!!.dismiss()

            // open chapter Activity && send data
            var intent = Intent(this, ChapterActivity::class.java)
            intent.putExtra(Const.CURRENT_CHAPTER_POSITION_INTENT, chapter.positionTag)
            startActivity(intent)

        }

    }

    override fun showViewLoadContinousChapterError() {
//        mDownLoadTruyenProgressDialog!!.dismiss()
        Const.toast("Tải chương thất bại !!! Vui lòng kiểm tra kết nối mạng và tải lại sau", this)
    }
}