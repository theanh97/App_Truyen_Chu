package com.theanhdev97.truyenchu.Model

import android.os.AsyncTask
import com.theanhdev97.truyenchu.Model.Callback.OnLoadListTypesResult
import com.theanhdev97.truyenhchu.Utils.Const
import com.theanhdev97.truyenhot.Utils.Internet
import org.jsoup.Jsoup
import java.io.IOException

/**
 * Created by DELL on 09/08/2017.
 */
class ListTypesHelper(url: String) {
    var mUrl: String? = null
    var mOnLoadListTypesListener: OnLoadListTypesResult? = null

    init {
        this.mUrl = url
    }

    fun setUrl(url: String) {
        this.mUrl = url
    }

    fun setOnLoadListTypesListener(onLoadListTypesResult: OnLoadListTypesResult) {
        this.mOnLoadListTypesListener = onLoadListTypesResult
    }

    fun getListTypes() {
        GetListTypesAsynctask().execute(mUrl)
    }

    inner class GetListTypesAsynctask() : AsyncTask<String, Void, String?>() {
        override fun doInBackground(vararg params: String?): String? {
            try {
                var content = Internet.downloadContent(params[0]!!)
                return content
            } catch (ex: IOException) {
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                var listTypes = ArrayList<TruyenType>()
                var doc = Jsoup.parse(result).select("div.dropdown-menu.multi-column").first()
                var root = doc.select("ul.dropdown-menu")
                for (i in root) {
                    var item = i.getElementsByTag("li")
                    for (j in item) {
                        var root2 = j.getElementsByTag("a").first()
                        var url = root2.attr("href")
                        var name = root2.text()
                        listTypes.add(TruyenType(name, url))
                    }
                }

                mOnLoadListTypesListener!!.onLoadListTypesSuccess(listTypes)
            } else {
                mOnLoadListTypesListener!!.onLoadListTypesFailure()
                this.cancel(true)
            }
        }
    }
}