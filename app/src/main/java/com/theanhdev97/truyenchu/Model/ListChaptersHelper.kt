package com.theanhdev97.truyenchu.Model

import android.os.AsyncTask
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListChaptersResult
import com.theanhdev97.truyenhchu.Utils.Const
import com.theanhdev97.truyenhot.Utils.Internet
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by DELL on 29/07/2017.
 */
class ListChaptersHelper(truyen: Truyen?) {

    var mTruyen: Truyen? = null
    var mOnLoadListChapterResult: OnLoadListChaptersResult? = null
    var mListChapters: ArrayList<MutableMap<Int, ArrayList<Chapter>>>? = null
    var mMainURL = ""
    var mMaxPageSize = 0
    var countItemsAddedToListChapters = 0

    init {
        this.mTruyen = truyen
        this.mMainURL = mTruyen!!.link
    }

    fun setTruyen(truyen: Truyen) {
        this.mTruyen = truyen
        this.mMainURL = truyen.link
    }

    fun setOnLoadListChapterResult(onLoadListChaptersResult: OnLoadListChaptersResult) {
        this.mOnLoadListChapterResult = onLoadListChaptersResult
    }

    fun getListChapter() {
        if (mTruyen!!.listChapter != null && (
                mMaxPageSize != null && mTruyen!!.listChapter!!.size == mMaxPageSize)) {
            mOnLoadListChapterResult!!.onLoadListChapterSuccess(mTruyen!!)
        } else {
            GetListChaptersAsynctask().execute(mTruyen!!.link)
        }
    }

    // get all list chapters page
    inner class GetListChaptersAsynctask : AsyncTask<String, Void, String?>() {
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
            if (result != null) {
                countItemsAddedToListChapters = 0
                mListChapters = ArrayList<MutableMap<Int, ArrayList<Chapter>>>()
                var doc = Jsoup.parse(result)
                var root = doc.select("ul.pagination.pagination-sm").select("li")

                // --- GET MAX PAGE SIZE --------
//                if (root.size > 0)
//                    mMaxPageSize = root[root.size - 2].getElementsByTag("a")[0].attr("title").split(" - Trang ")[1].toInt()
//                else
//                    mMaxPageSize = 1
                var ex = result.split("id=\"total-page\"")
                if (ex != null && ex.size > 0)
                    mMaxPageSize = (ex[1]
                            .split("\">")[0]
                            .split("value=\"")[1]).toInt()
                else
                    mMaxPageSize = 1

                for (i in 1..mMaxPageSize!!) {
                    var url: String = ""
                    if (i > 1) {
                        url = "${mMainURL!!}trang-${i}/#list-chapter"
                    } else
                        url = mMainURL!!
                    GetListChaptersInOnePage(i).execute(url)
                }
            } else {
                mOnLoadListChapterResult!!.onLoadListChapterFailure()
                this.cancel(true)
            }
        }
    }

    // get all list chapters in one page
    inner class GetListChaptersInOnePage(pageIndex: Int) : AsyncTask<String, Void, String?>() {
        var mIndex: Int? = null

        init {
            mIndex = pageIndex
        }

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
            if (result != null) {

                var doc = Jsoup.parse(result)
                var root = doc.select("div#list-chapter").first().select("div.row").first()

                var mainRoot = root.getElementsByTag("li")

                var listChapters = ArrayList<Chapter>()
                for (i in 0..mainRoot.size - 1) {
                    var information = mainRoot[i].getElementsByTag("a").attr("title")

                    var title = ""
                    var chapter = ""

                    var root = information.split(":")
                    if (root != null && root.size > 1) {
                        title = root[root.size -1 ]
                        var root1 = root[root.size - 2]

                        var root2 = root1.split(" - ")
                        chapter = root2[root2.size -1]
                    } else {
//                        var root2 = information.split(" - ")[1]
//                        chapter = "Chương " + root2.split("Chương ")[1]

                        var root2 = information.split(" - ")
                        if (root2.size > 2) {
                            title = root2[root2.size - 1]
                            chapter = root2[root2.size - 2]
                        }else
                            chapter= root2[root2.size -1 ]
                    }

//                    var link = "${mMainURL}chuong-${chapter}"
                    var link = mainRoot[i].getElementsByTag("a").attr("href")
                    listChapters.add(Chapter(link, title, chapter, ""))
                }
                // add To listChapters
                addItemToListChapters(listChapters, mIndex!! - 1)
            } else {
                mOnLoadListChapterResult!!.onLoadListChapterFailure()
                this.cancel(true)
            }
        }
    }

    fun addItemToListChapters(listChapters: ArrayList<Chapter>, index: Int) {
        var list = mutableMapOf<Int, ArrayList<Chapter>>(Pair(index, listChapters))
        mListChapters!!.add(countItemsAddedToListChapters, list)
        countItemsAddedToListChapters++
        if (countItemsAddedToListChapters == mMaxPageSize) {
            loadFullChapterCallback()
        }
    }

    fun loadFullChapterCallback() {
        // Sort Chapters
        Collections.sort(mListChapters, kotlin.Comparator { o1, o2 -> o1.keys.toIntArray()[0] - o2.keys.toIntArray()[0] })

        // add list chapters to Truyen
        mTruyen!!.listChapter = ArrayList<Chapter>()

        var index = 0
        for (item in mListChapters!!) {
            var arrayList = item.getValue(index)
            for (item2 in arrayList) {
                mTruyen!!.listChapter!!.add(item2)
            }
            index++
        }

        // callback
        mOnLoadListChapterResult!!.onLoadListChapterSuccess(mTruyen!!)
    }

}