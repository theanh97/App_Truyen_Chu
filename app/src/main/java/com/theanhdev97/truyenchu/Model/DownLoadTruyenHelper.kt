package com.theanhdev97.truyenchu.Model

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.support.v4.content.ContextCompat
import com.kaopiz.kprogresshud.KProgressHUD
import com.theanhdev97.truyenchu.Model.Callback.OnDownLoadTruyenResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadChapterResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListChaptersResult
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenhchu.Utils.Const

/**
 * Created by DELL on 04/08/2017.
 */
class DownLoadTruyenHelper(context: Context) :
        OnLoadChapterResult
/*, OnLoadListChaptersResult*/ {


    var mTruyen: Truyen? = null
    var mOnDownLoadTruyenResult: OnDownLoadTruyenResult? = null
    //    var mListChapterModel: ListChaptersHelper? = null
    var mChapterModel: ChapterHelper? = null
    var mDownLoadChaptersSize: Int? = null
    var mCountChaptersDownloaded = 0
    var mProgressDialog: KProgressHUD? = null
    var mContext: Context? = null

    init {

//        this.mListChapterModel = ListChaptersHelper(mTruyenDownLoadedBefore!!)
//        this.mListChapterModel!!.setOnLoadListChapterResult(this)

        this.mChapterModel = ChapterHelper()
        this.mChapterModel!!.setOnLoadChapterResult(this)
        this.mContext = context

        mProgressDialog = KProgressHUD.create(mContext)
                .setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent))
                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                .setLabel("Đang tải truyện !!!")
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
    }


    fun setTruyen(truyen: Truyen) {
        this.mTruyen = truyen.copy()
//        TruyenUtils.updateTruyenFromInternalStorage(mTruyen!!, mContext!!)
    }

    fun setOnDownTruyenListener(onDownLoadTruyenResult: OnDownLoadTruyenResult) {
        this.mOnDownLoadTruyenResult = onDownLoadTruyenResult
    }

    fun downLoadTruyen() {
        downLoadListChapters()
    }

    fun downLoadListChapters() {
        var list = mTruyen!!.listChapter!!
        var firstPositionDownload = 0
        // check download position
        for (i in 0 until list.size) {
            if (TruyenUtils.isEmptyChapter(list[i])) {
                firstPositionDownload = i
                break
            }
        }
        // Check if downloaded
        mCountChaptersDownloaded = 0
        mDownLoadChaptersSize = mTruyen!!.listChapter!!.size - firstPositionDownload!!
        // save if downloaded before is Full
        if (mDownLoadChaptersSize == 0) {
            TruyenUtils.saveTruyenDownLoaded(
                    mContext!!,
                    mTruyen!!.title,
                    mTruyen!!)
            TruyenHolder.getInstance().setCurTruyen(mTruyen)
            mOnDownLoadTruyenResult!!.onDownLoadTruyenSuccess()
        }
        // download missing chapters
        else {
            // set && show progress dialog
            mProgressDialog!!.setMaxProgress(mDownLoadChaptersSize!!+1)
            mProgressDialog!!.setProgress(0)
            mProgressDialog!!.show()

            // download
            var list = mTruyen!!.listChapter!!
            for (i in firstPositionDownload!! until list.size) {
                mChapterModel!!.getChapterByPosition(list[i], i)
            }
        }
    }

    override fun onLoadChapterSuccess(chapter: Chapter) {
        mTruyen!!.listChapter!![chapter.positionTag].content = chapter.content
        mCountChaptersDownloaded += 1
        mProgressDialog!!.setProgress(mCountChaptersDownloaded)
        // download success
        if (mCountChaptersDownloaded == mDownLoadChaptersSize) {
            Handler().post{
                TruyenUtils.saveTruyenDownLoaded(
                        mContext!!,
                        mTruyen!!.title,
                        mTruyen!!)
                TruyenHolder.getInstance().setCurTruyen(mTruyen)
                mOnDownLoadTruyenResult!!.onDownLoadTruyenSuccess()
                mProgressDialog!!.dismiss()
            }
        }
    }

    override fun onLoadChapterFailure() {
        mProgressDialog!!.dismiss()
        mOnDownLoadTruyenResult!!.onDownLoadTruyenFailure()
    }
}