package com.theanhdev97.truyenchu.View

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.theanhdev97.truyenchu.Model.TruyenHolder
import com.theanhdev97.truyenchu.R
import com.theanhdev97.truyenchu.Utils.TruyenUtils

class WaitingActivity : AppCompatActivity() {

//    var mGoToHomeViewHandler: GoToHomeViewHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)
        window.attributes.windowAnimations = R.style.translate
        initTruyenHolder()
        Handler().postDelayed({
            startActivity(Intent(this@WaitingActivity, HomeActivity::class.java))
            this.finish()
        }, 2000)
//        mGoToHomeViewHandler = GoToHomeViewHandler()
//        InitTruyenHodlerThread().run()
    }

    fun initTruyenHolder() {
        Handler().postDelayed({

            TruyenHolder.getInstance().setListDownloaded(
                    TruyenUtils.getListTruyenDownloaded(this))
            TruyenHolder.getInstance().setListReading(
                    TruyenUtils.getListTruyenReading(this))

        }, 200)
    }

//    inner class InitTruyenHodlerThread : Thread() {
//        override fun run() {
//            super.run()
//            TruyenHolder.getInstance().setListDownloaded(
//                    TruyenUtils.getListTruyenDownloaded(this@WaitingActivity))
//            TruyenHolder.getInstance().setListReading(
//                    TruyenUtils.getListTruyenReading(this@WaitingActivity))
//            mGoToHomeViewHandler!!.sendEmptyMessage(0)
//        }
//    }
//
//    inner class GoToHomeViewHandler : Handler() {
//        override fun handleMessage(msg: Message?) {
//            super.handleMessage(msg)
//            if (msg!!.what == 0) {
//                startActivity(Intent(this@WaitingActivity, HomeActivity::class.java))
//                this@WaitingActivity.finish()
//            }
//        }
//    }
}
