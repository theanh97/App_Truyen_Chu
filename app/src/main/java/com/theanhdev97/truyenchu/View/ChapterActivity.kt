package com.theanhdev97.truyenchu.View

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.transition.Fade
import android.transition.Slide
import android.view.*
import android.widget.AdapterView
import android.widget.RadioGroup
import android.widget.SeekBar
import com.theanhdev97.truyenchu.Presenter.ChapterPresenterImpl
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.View.Interface.ChapterView
import com.theanhdev97.truyenhchu.Utils.Const
import com.theanhdev97.truyenhot.Adapter.TextFontsAdapter
import kotlinx.android.synthetic.main.activity_chapter.*
import android.view.MotionEvent
import android.widget.CompoundButton
import com.kaopiz.kprogresshud.KProgressHUD
import com.theanhdev97.truyenchu.Model.*
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ChapterActivity : AppCompatActivity(),
        ChapterView,
        AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener,
        View.OnClickListener, View.OnTouchListener, CompoundButton.OnCheckedChangeListener {
    // MVP
    var mPresenter: ChapterPresenterImpl? = null
    var mModel: ChapterHelper? = null

    val SWIPE_THRESHOLD_VELOCITY = 20
    var mSharedPreferenced: SharedPreferences? = null
    var mSharedPreferencedEditor: SharedPreferences.Editor? = null
    var mChapterSetting: ChapterSetting? = null
    var mBottomSheetMenu: BottomSheetBehavior<View>? = null
    var mBottomSheetTextSetting: BottomSheetBehavior<View>? = null
    var mBottomSheetLightSetting: BottomSheetBehavior<View>? = null
    var mTruyen: Truyen? = null
    var mListFont: ArrayList<String>? = null
    var mListFontFromAsset: ArrayList<String>? = null
    var mListBackGroundColor: ArrayList<Int>? = null
    var mCurChapter: Chapter? = null
    var mCurChapterPosition: Int? = null
    var mNewChapterPosition: Int? = null
    var downX = 0f
    var upX = 0f
    var mProgressDialog: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_chapter)
//        window.attributes.windowAnimations = R.style.Fade
        initTransitionAnimation()
        setEventListener()
        initProgressDialog()
        initChapterSetting()
        setBottomSheetingDialog()
        mModel = ChapterHelper()
        mPresenter = ChapterPresenterImpl(this, mModel!!)

        initChapterData()
        mProgressDialog!!.show()
        updateSettingDialog(mChapterSetting!!)
        setCollapsingToolbar()
        setChapterUI(mChapterSetting)
        Handler().postDelayed({
            mPresenter!!.saveReadingChapterPositionOfTruyen()
            mProgressDialog!!.dismiss()
        }, 100)

//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,100 )
//        }, 2000)
//
//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,100 )
//        }, 5000)
//
//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,100 )
//        }, 8000)
//
//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,100 )
//        }, 11000)
//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,100 )
//        }, 14000)
//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,100 )
//        }, 17000)
//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,100 )
//        }, 20000)


//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,50)
//        }, 5000)
//
//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,50)
//        }, 6000)
//
//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,50)
//        }, 7000)
//
//        Handler().postDelayed({
//            nested_scrollview.smoothScrollBy(0,50)
//        }, 8000)
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

    override fun setCollapsingToolbar() {
        setSupportActionBar(toolbar)
        collapsing_toolbar!!.isTitleEnabled = false
        app_bar_layout.setExpanded(true)
        supportActionBar!!.setTitle("")
        val chapterTitle = "${mCurChapter!!.chapter} : ${mCurChapter!!.title}"
        tv_toolbar_title.text = chapterTitle

        // hiding & showing the title when toolbar expanded & collapsed
        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout
        .OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                Handler().postDelayed({
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    // COLLAPSED
                    if (scrollRange + verticalOffset == 0) {
                        isShow = true
                    }
                    // EXPANDED
                    else if (isShow) {
                        isShow = false
                        tv_toolbar_title.text = chapterTitle
                    }
                }, 10)
            }
        })
    }

    override fun setChapterUI(chapterSetting: ChapterSetting?) {
        tv_chapter_content!!.text = mCurChapter!!.content

        // light level
        if (chapterSetting!!.lightLevel != null)
            setLightLevelUI(chapterSetting!!.lightLevel!!)

        // background color
        if (chapterSetting!!.backgroundColor != null)
            setBackgroundColorUI(chapterSetting!!.backgroundColor!!)

        // text size
        if (chapterSetting!!.textSize != null)
            setTextSizeUI(chapterSetting!!.textSize!!)

        // text line spacing size
        if (chapterSetting!!.textLineSpacingSize != null)
            setTextLineSpacingUI(chapterSetting!!.textLineSpacingSize!!)

        // text font
        if (chapterSetting!!.textFont != null)
            setTextFontUI(chapterSetting!!.textFont!!)

        if (chapterSetting!!.isFullScreen != null)
            setFullScreenUI(chapterSetting!!.isFullScreen!!)
    }

    override fun showBottomSheetingDialog(isShow: Boolean, bottomSheetBehavior: BottomSheetBehavior<View>) {
        if (isShow)
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        else
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun setBottomSheetingDialog() {
        // menu
        mBottomSheetMenu = BottomSheetBehavior.from(bottom_sheet_menu)
        mBottomSheetMenu!!.state = BottomSheetBehavior.STATE_COLLAPSED

        // Text setting
        mBottomSheetTextSetting = BottomSheetBehavior.from(bottom_sheet_text_setting)
        mBottomSheetTextSetting!!.state = BottomSheetBehavior.STATE_COLLAPSED

        // Light setting
        mBottomSheetLightSetting = BottomSheetBehavior.from(bottom_sheet_light_setting)
        mBottomSheetLightSetting!!.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun showViewError() {
        mProgressDialog!!.dismiss()
        Const.toast("Xảy ra lỗi khi tải chương mới !!! kiểm tra kết nối mạng và thử tải lại",
                this)
    }

    override fun updateSettingDialog(chapterSetting: ChapterSetting) {
        val lightLevel = chapterSetting!!.lightLevel
        val backgroundColor = chapterSetting!!.backgroundColor
        val textSize = chapterSetting!!.textSize
        val textFont = chapterSetting!!.textFont
        val textLineSpacingSize = chapterSetting!!.textLineSpacingSize
        val isFullScreen = chapterSetting!!.isFullScreen

        runOnUiThread {
            // light level
            if (lightLevel != null) {
                mChapterSetting!!.lightLevel = lightLevel
                mSharedPreferencedEditor!!.putInt(Const.CHAPTER_LIGHT_LEVEL, lightLevel)
                sb_light_level.progress = lightLevel
            }

            // background color
            if (backgroundColor != null) {
                mChapterSetting!!.backgroundColor = backgroundColor
                mSharedPreferencedEditor!!.putInt(Const.CHAPTER_BACKGROUND_COLOR, backgroundColor)
                when (backgroundColor) {
                    0 -> group_rdb_background_color.check(R.id.rdb_color_1)
                    1 -> group_rdb_background_color.check(R.id.rdb_color_2)
                    2 -> group_rdb_background_color.check(R.id.rdb_color_3)
                    3 -> group_rdb_background_color.check(R.id.rdb_color_4)
                    4 -> group_rdb_background_color.check(R.id.rdb_color_5)
                    5 -> group_rdb_background_color.check(R.id.rdb_color_6)
                    6 -> group_rdb_background_color.check(R.id.rdb_color_7)
                    7 -> group_rdb_background_color.check(R.id.rdb_color_8)
                }
            }

            // text size
            if (textSize != null) {
                mChapterSetting!!.textSize = textSize
                tv_text_size.text = mChapterSetting!!.textSize.toString()
                mSharedPreferencedEditor!!.putInt(Const.CHAPTER_TEXT_SIZE, textSize)
            }

            // text line spacing size
            if (textLineSpacingSize != null) {
                mChapterSetting!!.textLineSpacingSize = textLineSpacingSize
                tv_line_space_size.text = mChapterSetting!!.textLineSpacingSize.toString()
                mSharedPreferencedEditor!!.putInt(Const.CHAPTER_TEXT_LINE_SPACING, textLineSpacingSize)
            }

            // text font
            if (textFont != null) {
                mChapterSetting!!.textFont = textFont
                mSharedPreferencedEditor!!.putString(Const.CHAPTER_TEXT_FONT, textFont)
                for (i in 0 until mListFont!!.size)
                    if (mListFont!!.get(i).equals(mChapterSetting!!.textFont)) {
                        spn_text_font.setSelection(i)
                        break
                    }
            }

            // Full screen
            if (isFullScreen != null) {
                mChapterSetting!!.isFullScreen = isFullScreen
                mSharedPreferencedEditor!!.putBoolean(Const.CHAPTER_FULL_SCREEN, isFullScreen)
                switch_full_screen.isChecked = isFullScreen
            }
            mSharedPreferencedEditor!!.commit()
        }
    }

    override fun initChapterData() {


        // list background color
        mListBackGroundColor = ArrayList<Int>()
        mListBackGroundColor!!.addAll(resources.getIntArray(R.array.chapter_background)
                .toList())

        // list font
        mListFont = ArrayList<String>()
        mListFont!!.addAll(resources.getStringArray(R.array.font_styles))

        // list font from asset
        mListFontFromAsset = ArrayList<String>()
        mListFontFromAsset!!.addAll(resources.getStringArray(R.array.font_styles_from_assets))

        // list font adapter
        var adapter = TextFontsAdapter(this, mListFont!!)
        spn_text_font.adapter = adapter

        mCurChapterPosition = intent.getIntExtra(Const.CURRENT_CHAPTER_POSITION_INTENT, -1)
        mTruyen = TruyenHolder.getInstance().getCurTruyen()
//        mTruyen = truyen


        mCurChapter = mTruyen!!.listChapter!![mCurChapterPosition!!]
    }

    override fun initChapterSetting() {
        // shared preferenced
        mSharedPreferenced = getSharedPreferences(Const.SHARED_PREFERENCED, Const.SHARED_PREFERENCED_MODE)
        mSharedPreferencedEditor = mSharedPreferenced!!.edit()

        // text size
        val textSize = mSharedPreferenced!!.getInt(Const.CHAPTER_TEXT_SIZE,
                Const.CHAPTER_TEXT_SIZE_DEFAULT)

        // chapter setting
        val textFont = mSharedPreferenced!!.getString(Const.CHAPTER_TEXT_FONT, Const
                .CHAPTER_TEXT_FONT_DEFAULT)

        // text line spacing size
        val textLineSpacingSize = mSharedPreferenced!!.getInt(Const.CHAPTER_TEXT_LINE_SPACING,
                Const.CHAPTER_TEXT_LINE_SPACING_DEFAULT)

        // light setting
        val lightLevel = mSharedPreferenced!!.getInt(Const.CHAPTER_LIGHT_LEVEL,
                Const.CHAPTER_LIGHT_LEVEL_DEFAULT)

        // background color
        val backgroundColor = mSharedPreferenced!!.getInt(Const.CHAPTER_BACKGROUND_COLOR,
                Const.CHAPTER_BACKGROUND_COLOR_DEFAULT)

        // Full screen
        val isFullScreen = mSharedPreferenced!!.getBoolean(Const.CHAPTER_FULL_SCREEN,
                Const.CHAPTER_FULL_SCREEN_DEFAULT)

        mChapterSetting = ChapterSetting(
                textSize,
                textLineSpacingSize,
                textFont,
                lightLevel,
                backgroundColor,
                isFullScreen)
    }

    override fun setFullScreen(isFullScreen: Boolean) {
        val setting = ChapterSetting(null, null, null, null, null, isFullScreen)
        updateSettingDialog(setting)
        setChapterUI(setting)
    }

    override fun setEventListener() {
        scv_chapter_content.setOnTouchListener(this)

        spn_text_font.setOnItemSelectedListener(this)
        group_rdb_background_color.setOnCheckedChangeListener(this)
        sb_light_level.setOnSeekBarChangeListener(this)
        imb_list_chapters.setOnClickListener(this)
        imb_light.setOnClickListener(this)
        imb_text.setOnClickListener(this)

        imb_descrease_text_size.setOnClickListener(this)
        imb_inscrease_text_size.setOnClickListener(this)
        imb_descrease_line_space.setOnClickListener(this)
        imb_inscrease_line_space.setOnClickListener(this)

        switch_full_screen.setOnCheckedChangeListener(this)
    }

    override fun setBackgroundColorUI(backgroundColor: Int) {
        runOnUiThread {
            tv_chapter_content.setBackgroundColor(mListBackGroundColor!!.get(backgroundColor))
            toolbar.setBackgroundColor(mListBackGroundColor!!.get(backgroundColor))

            if (backgroundColor == 7) {
                tv_chapter_content.setTextColor(mListBackGroundColor!!.get(6))
                tv_toolbar_title.setTextColor(mListBackGroundColor!!.get(6))
            } else if (backgroundColor == 6) {
                tv_chapter_content.setTextColor(mListBackGroundColor!!.get(7))
                tv_toolbar_title.setTextColor(mListBackGroundColor!!.get(7))
            } else {
                tv_chapter_content.setTextColor(mListBackGroundColor!!.get(6))
                tv_toolbar_title.setTextColor(mListBackGroundColor!!.get(6))
            }
        }
    }

    override fun setFullScreenUI(isFullScreen: Boolean) {
        if (isFullScreen == true)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun setLightLevelUI(lightLevel: Int) {
        runOnUiThread {
            val brightness = (lightLevel.toFloat() / 255f);
            var lp = getWindow().getAttributes();
            lp.screenBrightness = brightness;
            getWindow().setAttributes(lp);
        }
    }

    override fun setTextSizeUI(textSize: Int) {
        runOnUiThread {
            tv_chapter_content!!.textSize = textSize.toFloat()
        }
    }

    override fun setTextFontUI(textFont: String) {
        runOnUiThread {
            for (i in 0 until mListFont!!.size)
                if (mListFont!!.get(i).equals(textFont)) {
                    val fontStyle = mListFontFromAsset!!.get(i)
                    val face = Typeface.createFromAsset(assets, fontStyle)
                    tv_chapter_content.setTypeface(face, Typeface.NORMAL)
                    return@runOnUiThread
                }

        }
    }

    override fun setTextLineSpacingUI(textLineSpacingSize: Int) {
        runOnUiThread {
            tv_chapter_content.setLineSpacing(
                    0f,
                    textLineSpacingSize.toFloat())
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        mPresenter!!.onChangeFullScreen(isChecked)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
        // LIST CHAPTER
            R.id.imb_list_chapters -> {
                mPresenter!!.onSelectedBackListChapter()
            }
        // TEXT SETTING
            R.id.imb_text -> {
                mPresenter!!.onselectedTextSetting()
            }
        // --------TEXT SIZE---------
            R.id.imb_descrease_text_size -> {
                mPresenter!!.onChangedTextSize(false)
            }
            R.id.imb_inscrease_text_size -> {
                mPresenter!!.onChangedTextSize(true)
            }
        // ---- TEXT LINE SPACING SIZE -----
            R.id.imb_descrease_line_space -> {
                mPresenter!!.onChangedTextLineSpacingSize(false)
            }
            R.id.imb_inscrease_line_space -> {
                mPresenter!!.onChangedTextLineSpacingSize(true)
            }
        // -- LIGHT SETTING -----
            R.id.imb_light -> {
                showLightSetting()
            }
            else -> Const.log("Click else")
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.getAction()) {

            MotionEvent.ACTION_DOWN -> {
                downX = event!!.getX()
            }
            MotionEvent.ACTION_UP -> {
                upX = event!!.getX()
                val deltaX = downX - upX
                if (Math.abs(deltaX) > SWIPE_THRESHOLD_VELOCITY) {
                    Handler().post {
                        mProgressDialog!!.show()
                    }
                    if (deltaX > 0) {
                        mPresenter!!.onSwipeScreen(true)
                        return true
                    } else {
                        mPresenter!!.onSwipeScreen(false)
                        return true
                    }
                } else {
                    if (deltaX == 0f) {
                        if (mBottomSheetLightSetting!!.state == BottomSheetBehavior.STATE_COLLAPSED
                                && mBottomSheetMenu!!.state == BottomSheetBehavior.STATE_COLLAPSED
                                && mBottomSheetTextSetting!!.state == BottomSheetBehavior.STATE_COLLAPSED)
                            mPresenter!!.onClickedChapterContentScreen(true)
                        else
                            mPresenter!!.onClickedChapterContentScreen(false)
                        return true
                    } else
                        return false
                }
            }
        }
        return false
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // light level
        mPresenter!!.onChangedLightLevel()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        mPresenter!!.onChangedBackgroundColor(checkedId)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mPresenter!!.onChangedTextFont()
    }

    override fun backToListChapters() {
        finish()
    }

    override fun showLightSetting() {
        showBottomSheetingDialog(true, mBottomSheetLightSetting!!)
    }

    override fun showTextSetting() {
        showBottomSheetingDialog(true, mBottomSheetTextSetting!!)
    }

    override fun setTextFont() {
        val textFont = spn_text_font.selectedItem.toString()
        val setting = ChapterSetting(null, null, textFont, null, null, null)
        updateSettingDialog(setting)
        setChapterUI(setting)
    }

    override fun setBackgroundColor(colorID: Int) {
        var id = 0
        when (colorID) {
            R.id.rdb_color_1 -> {
                id = 0
            }
            R.id.rdb_color_2 -> {
                id = 1
            }
            R.id.rdb_color_3 -> {
                id = 2
            }
            R.id.rdb_color_4 -> {
                id = 3
            }
            R.id.rdb_color_5 -> {
                id = 4
            }
            R.id.rdb_color_6 -> {
                id = 5
            }
            R.id.rdb_color_7 -> {
                id = 6
            }
            R.id.rdb_color_8 -> {
                id = 7
            }
        }
        val setting = ChapterSetting(null, null, null, null, id, null)
        updateSettingDialog(setting)
        setChapterUI(setting)
    }

    override fun setLightLevel() {
        val setting = ChapterSetting(null, null, null, sb_light_level.progress, null, null)
        updateSettingDialog(setting)
        setChapterUI(setting)
    }

    override fun descreaseTextLineSpacingSize() {
        val textLineSpacingSize = tv_line_space_size.text.toString().toInt() - 1
        tv_line_space_size.text = textLineSpacingSize.toString()
        val setting = ChapterSetting(null, textLineSpacingSize, null, null, null, null)
        updateSettingDialog(setting)
        setChapterUI(setting)
    }

    override fun inscreaseTextLineSpacingSize() {
        val textLineSpacingSize = tv_line_space_size.text.toString().toInt() + 1
        tv_line_space_size.text = textLineSpacingSize.toString()
        val setting = ChapterSetting(null, textLineSpacingSize, null, null, null, null)
        updateSettingDialog(setting)
        setChapterUI(setting)
    }

    override fun descreaseTextSize() {
        var textSize = tv_text_size.text.toString().toInt() - 1
        tv_text_size.text = textSize.toString()
        val setting = ChapterSetting(textSize, null, null, null, null, null)
        updateSettingDialog(setting)
        setChapterUI(setting)
    }

    override fun inscreaseTextSize() {
        var textSize = tv_text_size.text.toString().toInt() + 1
        tv_text_size.text = textSize.toString()
        val setting = ChapterSetting(textSize, null, null, null, null, null)
        updateSettingDialog(setting)
        setChapterUI(setting)
    }

    override fun showMenuSetting() {
        showBottomSheetingDialog(true, mBottomSheetMenu!!)
    }

    override fun hideAllBottomShettingDialog() {
        runOnUiThread {
            // bottom sheet
            mBottomSheetMenu!!.state = BottomSheetBehavior.STATE_COLLAPSED
            mBottomSheetLightSetting!!.state = BottomSheetBehavior.STATE_COLLAPSED
            mBottomSheetTextSetting!!.state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }

    override fun checkHasNextChapter(): Chapter? {
        mNewChapterPosition = mCurChapterPosition!! + 1
        if (mNewChapterPosition!! < mTruyen!!.listChapter!!.size) {
            return mTruyen!!.listChapter!![mNewChapterPosition!!]
        } else
            return null
    }

    override fun checkHasPrevChapter(): Chapter? {
        mNewChapterPosition = mCurChapterPosition!! - 1
        if (mNewChapterPosition!! >= 0) {
            return mTruyen!!.listChapter!![mNewChapterPosition!!]
        } else
            return null
    }

    override fun showNotHasNextChapter() {
        Handler().postDelayed({
            mProgressDialog!!.dismiss()
            Const.toast("Hiện tại chưa có chương tiếp theo !!!", this)
        }, 30)
    }

    override fun showNotHasPrevChapter() {
        Handler().postDelayed({
            mProgressDialog!!.dismiss()
            Const.toast("Đây là chương đầu tiên !!!", this)
        }, 30)
    }

    override fun reloadNewChapterUI(newChapter: Chapter) {
        // update truyen data
        updateNewChapter(newChapter)

        // update chapter content
        tv_chapter_content.text = mCurChapter!!.content

        // update toolbal title
        setCollapsingToolbar()


        // go to Top content
        nested_scrollview.smoothScrollTo(5, 10);
    }

    override fun saveReadingChapterPosition() {
        TruyenUtils.saveReadingTruyen(
                this, Truyen(
                mTruyen!!.link, mTruyen!!.title, "", mTruyen!!.imageOne,
                mTruyen!!.author, 0.0, "", "",
                "", "", null, mCurChapterPosition!!))
        Handler().postDelayed({
            mProgressDialog!!.dismiss()
        }, 30)
    }

    override fun updateNewChapter(newChapter: Chapter) {
        mCurChapterPosition = mNewChapterPosition
        mCurChapter = newChapter
        mTruyen!!.listChapter!![mCurChapterPosition!!] = newChapter
    }
}
