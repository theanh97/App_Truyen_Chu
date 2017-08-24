package com.theanhdev97.truyenchu.View

import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.View.Interface.HomeView
import kotlinx.android.synthetic.main.activity_home.*
import android.R.attr.textColorPrimary
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.Slide
import android.view.ContextMenu
import android.view.View
import com.theanhdev97.truyenchu.Adapter.ChaptersAdapter
import com.theanhdev97.truyenchu.Adapter.HorizontalTruyenByTypeAdapter
import com.theanhdev97.truyenchu.Adapter.MyFragmentPagerAdapter
import com.theanhdev97.truyenchu.Model.*
import com.theanhdev97.truyenchu.Presenter.HomePresenterImpl
import com.theanhdev97.truyenchu.Presenter.Interface.HomePresenter
import com.theanhdev97.truyenchu.R.id.*
import com.theanhdev97.truyenchu.Utils.SimpleDividerItemDecoration
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenhchu.Utils.Const
import com.theanhdev97.truyenhchu.Utils.InternalStorage
import kotlinx.android.synthetic.main.activity_list_chapters.*


class HomeActivity : AppCompatActivity()
        , HomeView, BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    // MVP
    var mPresenter: HomePresenterImpl? = null

    var mPagerAdapter: MyFragmentPagerAdapter? = null
    var mListFragment: ArrayList<Fragment>? = null
    var mMyHomeFragment: MyHomeFragment? = null
    var mHomePageFragment: HomePageFragment? = null
    var mListTypeFragment: ListTypesFragment? = null
    var mInformationDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        window.attributes.windowAnimations = R.style.translate
        initTransitionAnimation()
        initDialog()
        initToolbar()
        initViewPager()
        setEventListener()
        mPresenter = HomePresenterImpl(this)
        requestPermissions()
    }

    override fun initTransitionAnimation() {
        val fade = Fade()
        fade.setDuration(1500)
        window.enterTransition = fade

        val slide = Slide()
        fade.setDuration(1500)
        window.returnTransition = slide
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Const.REQUEST_PERMISSION_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Granted.

                } else {
                    //Denied.
                    Const.toast("bạn không thể lưu truyện nếu không cho phép lưu vào bộ nhớ ngoài" +
                            " !!!",this)
                }
        }
    }

    override fun requestPermissions() {
        Handler().postDelayed({
            ActivityCompat.requestPermissions(this,
                    Const.REQUEST_PERMISSIONS,
                    Const.REQUEST_PERMISSION_CODE)
        }, 50)
    }

    override fun onBackPressed() {
        mPresenter!!.onBackPressed()
    }

    override fun exitApp() {
        System.exit(0)
    }

    override fun showPressBackAgainToExitApp() {
        Const.toast("Nhấn Back thêm lần nữa để thoát ứng dụng !!!", this)
    }

    override fun initViewPager() {
        runOnUiThread {
            vpg_home.offscreenPageLimit = 2
            mPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)
            mHomePageFragment = HomePageFragment()
            mListTypeFragment = ListTypesFragment()
            mMyHomeFragment = MyHomeFragment()

            mListFragment = ArrayList<Fragment>()
            mListFragment!!.add(mHomePageFragment!!)
            mListFragment!!.add(mListTypeFragment!!)
            mListFragment!!.add(mMyHomeFragment!!)
            mPagerAdapter!!.listFragments = mListFragment
            vpg_home.adapter = mPagerAdapter
        }
    }

    override fun initDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.information_dialog_title))
        builder.setMessage(getString(R.string.information_dialog_content))

        val positiveText = getString(R.string.information_dialog_button)
        builder.setPositiveButton(positiveText
        ) { dialog, which ->
            dialog.dismiss()
        }

        mInformationDialog = builder.create()
    }

    override fun initToolbar() {
        setSupportActionBar(tool_bar)
    }

    override fun setEventListener() {
        navigation.setOnNavigationItemSelectedListener(this)
        vpg_home.addOnPageChangeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        mPresenter!!.onSelectedAnotherPage(position, true)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                mPresenter!!.onSelectedAnotherPage(0, false)
                return true
            }
            R.id.action_list_types -> {
                mPresenter!!.onSelectedAnotherPage(1, false)
                return true
            }
            R.id.action_my_home -> {
                mPresenter!!.onSelectedAnotherPage(2, false)
                return true
            }
            else -> {
                return false
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_search -> mPresenter!!.onSelectedSearchMenu()
            R.id.action_information_app -> mPresenter!!.onSelectedInformationMenu()
            R.id.action_rate_app -> mPresenter!!.onSelectedRatingApp()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun goToRatingAppView() {
        val uri = Uri.parse("market://details?id=" + getString(R.string.app_id));
        var goToMarket = Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        try {
            startActivity(goToMarket);
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" +
                            getString(R.string.app_id))));
        }
    }

    override fun goToHomePageView(isScrolling: Boolean) {
        tv_toolbar_title.text = "Trang chủ"
        if (isScrolling)
            navigation.selectedItemId = R.id.action_home
        else
            vpg_home.setCurrentItem(0, true)
    }

    override fun goToListTypesView(isScrolling: Boolean) {
        tv_toolbar_title.text = "Thể loại"
        if (isScrolling)
            navigation.selectedItemId = R.id.action_list_types
        else
            vpg_home.setCurrentItem(1, true)
    }

    override fun goToMyHomeView(isScrolling: Boolean) {
        tv_toolbar_title.text = "Của bạn"
        if (isScrolling)
            navigation.selectedItemId = R.id.action_my_home
        else {
            vpg_home.setCurrentItem(2, true)
        }

    }

    override fun goToSearchView() {
        startActivity(Intent(this, SearchTruyenActivity::class.java))
    }

    override fun showInformationDialog() {
        mInformationDialog!!.show()
    }
}
