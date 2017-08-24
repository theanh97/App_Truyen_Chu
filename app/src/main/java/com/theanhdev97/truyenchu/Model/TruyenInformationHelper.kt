package com.theanhdev97.truyenchu.Model

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListChaptersResult
import com.theanhdev97.truyenchu.Model.Callback.OnLoadTruyenInformationResult
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenhot.Utils.Internet
import org.jsoup.Jsoup
import java.io.IOException

/**
 * Created by DELL on 29/07/2017.
 */
class TruyenInformationHelper(context: Context) : OnLoadListChaptersResult {
    var mTruyen: Truyen? = null
    var mOnLoadTruyenInformationResult: OnLoadTruyenInformationResult? = null
    var mListChapterModel: ListChaptersHelper? = null
    var mContext: Context? = null

    init {
        this.mContext = context
    }

    fun setTruyen(truyen: Truyen) {
        this.mTruyen = truyen
        this.mListChapterModel = ListChaptersHelper(mTruyen)
        this.mListChapterModel!!.setOnLoadListChapterResult(this)
        this.mListChapterModel!!.setTruyen(mTruyen!!)
    }

    fun setOnLoadTruyenResult(onLoadListTruyenInformationResult: OnLoadTruyenInformationResult) {
        this.mOnLoadTruyenInformationResult = onLoadListTruyenInformationResult
    }

    fun getTruyen() {
        DownLoadTruyenInformationAsyncTask().execute(mTruyen!!.link)
    }

    // down load truyen information
    inner class DownLoadTruyenInformationAsyncTask : AsyncTask<String, Void, String?>() {

        override fun doInBackground(vararg params: String?): String? {
            var url = params[0]
            try {
                var content = Internet.downloadContent(url!!)
                return content
            } catch(io: IOException) {
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {

                // get information
                var doc = Jsoup.parse(result)
                var root = doc.select("div.col-xs-12.col-info-desc")
                var root1 = root.select("div.col-xs-12")[0]
                var root2 = root.select("div.col-xs-12")[2]
                var root3 = root1.select("div.info")

                // IMAGE TWO
                var imageTwo = root1.select("div.book")[0].getElementsByTag("img").attr("src").toString()

                // AUTHOR
                var author = root3[0].getElementsByTag("a")[0].text()

//                // TYPE
//                var rootType = root3[0].getElementsByTag("div")[0].getElementsByTag("a")
//                var content1 = StringBuilder()
//                for (index in 1..rootType.size - 1)
//                    content1.append(", ${rootType[index].text()}")
//                var type = content1.toString().split(" , ")[0]
//                if (type != null)
//                    type = type.removeRange(0, 2)
                var type = ""

                // STATUS
                var status: String = ""
                if (root3[0].getElementsByTag("span").size > 1)
                    status = root3[0].getElementsByTag("span")[1].text()
                else
                    status = root3[0].getElementsByTag("span")[0].text()


                // DESCRIPTION
                var ex = result.split("<div class=\"desc-text")
                var rootDescription = ex[ex.size - 1]
                        .split("description\">")[1]
                        .split("</div>")[0]

                var description = rootDescription.replace("<br><br>", "\n\n")
                        .replace("<br>", "\n\n")
                        .replace("<h2>", "")
                        .replace("</h2>", "")
                        .replace("&nbsp;&nbsp;", " ")
                        .replace("&nbsp;", " ")
                        .replace("<span>", "")
                        .replace("</span>", "")
                        .replace("</br>", "\n\n")
                        .replace("</p><p>", "\n\n")
                        .replace("<p>", "")
                        .replace("</p>", "")
                        .replace("<b>", "")
                        .replace("</b>", "")
                        .replace("</strong>", "")
                        .replace("<strong>", "")
                        .replace("<i>", "")
                        .replace("</i>", "")
                        .replace("</a>", "")
                        .replace(Regex("(<a.+>)"), "")
                        .replace(Regex("(Thể loại:.+\n+|Chuyển ngữ.+\n+|Edit.+\n+)"), "")
                        .replace(Regex("(Biên tập viên:.+\n|Số chương:.+\n)"), "")
                        .replace(Regex("(<em>.+)"), "")
                        .replace(Regex("\n\n\n\n"), "\n\n")

                // TITLE
                var title = root2.select("h3.title").first().text()

                // RATING
                var rating = "0.0"
                var rootRating = root2.getElementsByTag("span").first()
                if (rootRating != null && !rootRating.text().equals(""))
                    rating = rootRating.text()

                // OBJECT
                mTruyen!!.title = title
                mTruyen!!.author = author
                mTruyen!!.rate = rating!!.toDouble()
                mTruyen!!.description = description
                mTruyen!!.type = type
                mTruyen!!.status = status
                mTruyen!!.imageTwo = imageTwo

                mListChapterModel!!.getListChapter()
            } else {
                mTruyen = TruyenUtils.getDownLoadedTruyenByTenTruyen(mTruyen!!.title)
                if (mTruyen == null) {
                    mOnLoadTruyenInformationResult!!.onLoadTruyenInformationFailure()
                } else {
                    mOnLoadTruyenInformationResult!!.onLoadTruyenInformationSuccess(mTruyen!!)
                }
            }
        }
    }

    override fun onLoadListChapterSuccess(truyen: Truyen) {
        Handler().post({
            mTruyen!!.listChapter = truyen.listChapter
            TruyenUtils!!.updateTruyenFromInternalStorage(mTruyen!!, mContext!!)
            mOnLoadTruyenInformationResult!!.onLoadTruyenInformationSuccess(mTruyen!!)
        })
    }

    override fun onLoadListChapterFailure() {
        mTruyen = TruyenUtils.getDownLoadedTruyenByTenTruyen(mTruyen!!.title)
        if (mTruyen == null)
            mOnLoadTruyenInformationResult!!.onLoadTruyenInformationFailure()
        else
            mOnLoadTruyenInformationResult!!.onLoadTruyenInformationSuccess(mTruyen!!)
    }
}

