package com.theanhdev97.truyenchu.Model

import android.os.AsyncTask
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListTruyenResult
import com.theanhdev97.truyenhchu.Utils.Const
import com.theanhdev97.truyenhot.Utils.Internet
import org.jsoup.Jsoup
import java.io.IOException

/**
 * Created by DELL on 28/07/2017.
 */
class ListTruyenHelper(url: String) {
    var mUrl: String? = null
    var mOnLoadListTruyenResult: OnLoadListTruyenResult? = null
    var mDownLoadListTruyenAsyncTask: DownLoadListTruyenAsyncTask? = null

    init {
        this.mUrl = url
    }

    fun setOnLoadListTruyenResult(onLoadListTruyenResult: OnLoadListTruyenResult) {
        this.mOnLoadListTruyenResult = onLoadListTruyenResult
    }

    fun setListTruyenUrl(url: String) {
        this.mUrl = url
    }

    fun getListTruyen() {
        mDownLoadListTruyenAsyncTask = DownLoadListTruyenAsyncTask()
        mDownLoadListTruyenAsyncTask!!.execute(mUrl)
    }

    fun stopAsynctask() {
        if (mDownLoadListTruyenAsyncTask != null &&
                mDownLoadListTruyenAsyncTask!!.getStatus() != AsyncTask.Status.RUNNING)
            mDownLoadListTruyenAsyncTask!!.cancel(true);
    }

    inner class DownLoadListTruyenAsyncTask : AsyncTask<String, Void, String?>() {
        override fun doInBackground(vararg params: String?): String? {
            var url = params[0]
            try {
                var content = Internet.downloadContent(url!!)
                return content
            } catch (io: IOException) {
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            // success
            if (result != null) {
                var listTruyen = ArrayList<Truyen>()
                var doc = Jsoup.parse(result)
                var root = doc.select("div.col-xs-12.col-sm-12.col-md-9.col-truyen-main")
                var array = root.select("div.row")
                for (it in array) {
                    // LINK
                    var link = it.getElementsByTag("a").attr("href")

                    // IMAGE
                    var src = it.getElementsByAttribute("data-image").attr("data-image")

                    // MAX CHAPTER
                    var rootMaxChapter = it.getElementsByTag("a").text().split("Chương ")
                    var maxChapter = "Chưa có chương"
                    if (rootMaxChapter.size > 1)
                        maxChapter = rootMaxChapter[1]

                    // TITLE
                    var title = it.getElementsByTag("a").text()
                            .split("Chương")[0]
                            .replace(Regex("(\\s+$)"),"")
                    // AUTHOR
                    var author = it.select("span.author").text()

                    // ADD TO ARRAY
                    listTruyen!!.add(Truyen(link, title, maxChapter, src, author, 0.0, "", "",
                            "", "", null))
                }
                mOnLoadListTruyenResult!!.onLoadListTruyenSuccess(listTruyen!!)
            }
            // Failure
            else
                mOnLoadListTruyenResult!!.onLoadListTruyenFailure()
        }
    }
}