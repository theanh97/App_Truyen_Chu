package com.theanhdev97.truyenchu.Model

import android.os.AsyncTask
import com.theanhdev97.truyenchu.Model.Callback.OnSearchTruyenResult
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenhchu.Utils.Const
import com.theanhdev97.truyenhot.Utils.Internet
import org.jsoup.Jsoup
import java.io.IOException

/**
 * Created by DELL on 13/08/2017.
 */
class SearchTruyenHelper {
    var mKeyWordUrl: String? = null
    var mOnSearchTruyenListener: OnSearchTruyenResult? = null
    var isFirstLoad = false

    fun setOnSearchTruyenListener(onSearchTruyenResult: OnSearchTruyenResult) {
        this.mOnSearchTruyenListener = onSearchTruyenResult
    }

    fun setKeyWords(keyWords: String) {
        this.mKeyWordUrl = TruyenUtils.getSearchTruyenUrl(keyWords, 1)
    }

    fun getListTruyens() {
        isFirstLoad = true
        GetListTruyenByKeyWordsAsyncTask().execute(mKeyWordUrl)
    }

    fun getListTruyensByPostion(position: Int) {
        GetListTruyenByKeyWordsAsyncTask().execute(TruyenUtils.getSearchTruyenUrl(
                mKeyWordUrl!!,
                position))
        Const.log("LINK : ${TruyenUtils.getSearchTruyenUrl(
                mKeyWordUrl!!,
                position)}")
    }

    inner class GetListTruyenByKeyWordsAsyncTask : AsyncTask<String, Void, String?>() {

        override fun doInBackground(vararg params: String?): String? {
            try {
                var content = Internet.downloadContent(params[0]!!)
                return content
            } catch(ex: IOException) {
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                var listTruyens = ArrayList<Truyen>()

                var doc = Jsoup.parse(result)
                var root = doc.select("div.col-xs-12.col-sm-12.col-md-9.col-truyen-main")
                var array = root.select("div.row")
                // max search Page size

                // --- GET MAX PAGE SIZE --------
                var maxPageSearchSize = 1
                if (isFirstLoad == true) {
                    var rootMaxSearchPageSize = doc.select("ul.pagination.pagination-sm")
                    if (rootMaxSearchPageSize == null)
                        maxPageSearchSize = 1
                    else {
                        var ex = rootMaxSearchPageSize.select("li")
                        if (ex.size > 1)
                            maxPageSearchSize = ex[ex.size - 2]
                                    .getElementsByTag("a")
                                    .first()
                                    .attr("href")
                                    .split("page=")[1].toInt()
                        else
                            maxPageSearchSize = 1
                    }
                    isFirstLoad = false
                }

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
                    listTruyens!!.add(Truyen(link, title, maxChapter, src, author, 0.0, "", "",
                            "", "", null))
                }
//                for (i in listTruyens)
//                    Const.log(i.toString())
                mOnSearchTruyenListener!!.onSearchTruyenSuccess(listTruyens, maxPageSearchSize!!)
            } else {
                mOnSearchTruyenListener!!.onSearchTruyenFailure()
                this.cancel(true)
            }
        }
    }
}