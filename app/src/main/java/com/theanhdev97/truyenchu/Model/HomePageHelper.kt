package com.theanhdev97.truyenchu.Model

import android.os.AsyncTask
import com.theanhdev97.truyenchu.Model.Callback.OnLoadHomePageResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListTruyenResult
import com.theanhdev97.truyenhchu.Utils.Const
import com.theanhdev97.truyenhot.Utils.Internet
import org.jsoup.Jsoup
import org.jsoup.select.Selector.select

/**
 * Created by DELL on 06/08/2017.
 */
class HomePageHelper : OnLoadListTruyenResult {
    var mOnLoadHomePageListener: OnLoadHomePageResult? = null
    var mListTruyenModel: ListTruyenHelper? = null
    var mListTruyenHot: ArrayList<Truyen>? = null
    var mListTruyenHoanThanh: ArrayList<Truyen>? = null
    var mListTruyenMoi: ArrayList<Truyen>? = null
    var position = 0

    init {
        mListTruyenModel = ListTruyenHelper(Const.TRUYEN_HOT_URL)
        mListTruyenModel!!.setOnLoadListTruyenResult(this)
    }

    fun setOnLoadHomePageListener(onLoadHomePageResult: OnLoadHomePageResult) {
        this.mOnLoadHomePageListener = onLoadHomePageResult
    }

    fun loadHomePage() {
        position = 0
        mListTruyenModel!!.setListTruyenUrl(Const.TRUYEN_HOT_URL)
        mListTruyenModel!!.getListTruyen()
    }


    override fun onLoadListTruyenSuccess(listTruyen: ArrayList<Truyen>) {
        when (position) {
            0 -> {
                mListTruyenHot = ArrayList<Truyen>()
                mListTruyenHot!!.addAll(listTruyen)
                mListTruyenModel!!.setListTruyenUrl(Const.TRUYEN_FULL_URL)
                mListTruyenModel!!.getListTruyen()
            }
            1 -> {
                mListTruyenHoanThanh = ArrayList<Truyen>()
                mListTruyenHoanThanh!!.addAll(listTruyen)
                mListTruyenModel!!.setListTruyenUrl(Const.TRUYEN_MOI_URL)
                mListTruyenModel!!.getListTruyen()
            }
            2 -> {
                mListTruyenMoi = ArrayList<Truyen>()
                mListTruyenMoi!!.addAll(listTruyen)
                mOnLoadHomePageListener!!.onLoadHomePageSuccess(
                        mListTruyenHot!!,
                        mListTruyenHoanThanh!!,
                        mListTruyenMoi!!)
            }
            else -> {
            }
        }
        position++
    }

    override fun onLoadListTruyenFailure() {
        // stop async
        mListTruyenModel!!.stopAsynctask()
//        mListTruyenMoiModel!!.stopAsynctask()
//        mListTruyenHoanThanhModel!!.stopAsynctask()

        mOnLoadHomePageListener!!.onLoadHomePageFailure()
    }


//    inner class LoadHomePageAsynctask : AsyncTask<Void, Void, String?>() {
//        override fun doInBackground(vararg params: Void?): String? {
//            var content = Internet.downloadContent(HOME_URL)
//            return content
//        }
//
//        override fun onPostExecute(result: String?) {
//            if (result != null) {
//                var jsoup = Jsoup.parse(result)
//
//                var listTruyenHot = ArrayList<Truyen>()
//                var listTruyenHoanThanh = ArrayList<Truyen>()
//
//                var rootHot = jsoup.select("div#intro-index").first().select("div.item")
//                for (i in rootHot) {
//                    var title = i.getElementsByTag("h3").first().text()
//                    var link = i.getElementsByTag("a").first().attr("href")
//                    var imageOne = i.getElementsByTag("img").first().attr("src")
//                    var truyen = Truyen(link, title, "", imageOne, "", 0.0, "", "", "", "", null)
//                    listTruyenHot.add(truyen)
//                }
//
//                var rootHoanThanh = jsoup.select("div#truyen-slide").first()
//                        .select("div.row").first()
//                        .select("div.col-xs-4")
//                for (i in rootHoanThanh) {
//                    var title = i.getElementsByTag("h3").first().text()
//                    var link = i.getElementsByTag("a").first().attr("href")
//                    var imageOne = i.getElementsByTag("a").first().getElementsByAttribute("data-desk-image")
//                            .first().attr("data-image")
//                    var truyen = Truyen(link, title, "", imageOne, "", 0.0, "", "", "", "", null)
//                    listTruyenHoanThanh.add(truyen)
//                }
//
////                var rootMoiCapNhat = jsoup.select("div#list-index")
////                Const.log("\n\n\t\t\t${rootMoiCapNhat.html()}")
////
////                var rootHoanThanh = jsoup.select("div#truyen-slide")
////                Const.log("\n\n\t\t\t${rootHoanThanh.html()}")
//                mOnLoadHomePageListener!!.onLoadHomePageSuccess(listTruyenHot, listTruyenHoanThanh)
//            } else
//                mOnLoadHomePageListener!!.onLoadHomePageFailure()
//        }
//    }
}