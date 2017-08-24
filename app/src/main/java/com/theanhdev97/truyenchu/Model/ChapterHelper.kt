package com.theanhdev97.truyenchu.Model

import android.os.AsyncTask
import com.theanhdev97.truyenchu.Model.Callback.OnLoadChapterResult
import com.theanhdev97.truyenchu.Utils.TruyenUtils
import com.theanhdev97.truyenhchu.Utils.Const
import com.theanhdev97.truyenhot.Utils.Internet
import java.io.IOException

/**
 * Created by DELL on 29/07/2017.
 */
class ChapterHelper() {
    var mChapter: Chapter? = null
    var mOnLoadChapterResult: OnLoadChapterResult? = null

    fun setOnLoadChapterResult(onLoadChapterResult: OnLoadChapterResult) {
        this.mOnLoadChapterResult = onLoadChapterResult
    }

    fun setChapter(chapter: Chapter) {
        this.mChapter = chapter
    }

    fun getChapterByPosition(chapter: Chapter, position: Int) {
        if (!TruyenUtils.isEmptyChapter(chapter!!)) {
            chapter!!.positionTag = position
            mOnLoadChapterResult!!.onLoadChapterSuccess(chapter)
        } else
            GetChapterByPositionAsynctask(chapter, position).execute(chapter.link)
    }

    fun getChapter() {
        if (!TruyenUtils.isEmptyChapter(mChapter!!))
            mOnLoadChapterResult!!.onLoadChapterSuccess(mChapter!!)
        else
            GetChapterAsynctask().execute(mChapter!!.link)
    }

    inner class GetChapterAsynctask : AsyncTask<String?, Void, String?>() {


        override fun doInBackground(vararg params: String?): String? {
            try {
                val content = Internet.downloadContent(params[0]!!)
                return content
            } catch(io: IOException) {
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                // CHAPTER CONTENT
                var root = result.split("<div class=\"chapter-c")
                var chapterContent = root.get(root.size - 1)
                        .split("</div>")[0].replace("\">", "\t\t")
                        .replace("<i>", "")
                        .replace("</i>", "")
                        .replace("<em>", "")
                        .replace("</em>", "")
                        .replace("<strong>", "")
                        .replace("</strong>", "")
                        .replace("&quot;", "")
                        .replace("<br>", "\n\n\t\t")
                        .replace("</br>", "\n\n\t\t")
                        .replace("<br><br>", "\n\n\t\t")
                        .replace("<br/><br/>", "\n\n\t\t")
                        .replace("<br></br>", "\n\n\t\t")
                        .replace("</p><p>", "\n\n\t\t")
                        .replace("&#8211;", "-")
                        .replace("&nbsp;", " ")
                        .replace("&nbsp\\;", " ")
                        .replace("\\&nbsp\\;", " ")
                        .replace("\\&nbsp;", " ")
                        .replace("<b>", "")
                        .replace("</b>", "")
                        .replace("</p>", "")
                        .replace("<p>", "")
                        .replace("</b>", "")
                        .replace("</span>", "")
                        .replace("<span>", "")

                        // ------replace error font--------------
                        // grave -> " ` "
                        .replace("&agrave;", "à")
                        .replace("&Agrave;", "À")
                        .replace("&ograve;", "ò")
                        .replace("&igrave;", "ì")
                        .replace("&ygrave;", "ỳ")
                        .replace("&ugrave;", "ù")
                        .replace("&Ugrave;", "Ù")
                        .replace("&egrave;", "è")

                        // circ; -> " ^ "
                        .replace("&ecirc;", "ê")
                        .replace("&acirc;", "â")
                        .replace("&ocirc;", "ô")

                        // acute; -> " ' "
                        .replace("&aacute;", "á")
                        .replace("&Aacute;", "Á")
                        .replace("&oacute;", "ó")
                        .replace("&Oacute;", "Ó")
                        .replace("&iacute;", "í")
                        .replace("&yacute;", "ý")
                        .replace("&uacute;", "ú")
                        .replace("&eacute;", "é")
                        .replace("&Eacute;", "É")

                        // tilde; -> " ~ "
                        .replace("&atilde;", "ã")
                        .replace("&otilde;", "õ")
                        .replace("&itilde;", "ĩ")
                        .replace("&ytilde;", "ỹ")
                        .replace("&utilde;", "ũ")

                mChapter!!.content = chapterContent
                mOnLoadChapterResult!!.onLoadChapterSuccess(mChapter!!)
            } else {
                mOnLoadChapterResult!!.onLoadChapterFailure()
                this.cancel(true)
            }
        }
    }

    inner class GetChapterByPositionAsynctask(chapter: Chapter, position: Int) :
            AsyncTask<String?, Void, String?>() {
        var pos: Int = 0
        var ct: Chapter? = null

        init {
            pos = position
            ct = chapter
        }

        override fun doInBackground(vararg params: String?): String? {
            try {
                val content = Internet.downloadContent(params[0]!!)
                return content
            } catch(io: IOException) {
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                // CHAPTER CONTENT
                var root = result.split("<div class=\"chapter-c")
                var chapterContent = root.get(root.size - 1)
                        .split("</div>")[0].replace("\">", "\t\t")
                        .replace("<i>", "")
                        .replace("</i>", "")
                        .replace("<em>", "")
                        .replace("</em>", "")
                        .replace("<strong>", "")
                        .replace("</strong>", "")
                        .replace("&quot;", "")
                        .replace("<br>", "\n\n\t\t")
                        .replace("</br>", "\n\n\t\t")
                        .replace("<br><br>", "\n\n\t\t")
                        .replace("<br></br>", "\n\n\t\t")
                        .replace("<br/><br/>", "\n\n\t\t")
                        .replace("</p><p>", "\n\n\t\t")
                        .replace("&#8211;", "-")
                        .replace("&nbsp;", " ")
                        .replace("&nbsp\\;", " ")
                        .replace("\\&nbsp\\;", " ")
                        .replace("\\&nbsp;", " ")
                        .replace("<b>", "")
                        .replace("</b>", "")
                        .replace("</p>", "")
                        .replace("<p>", "")
                        .replace("</b>", "")
                        .replace("</span>", "")
                        .replace("<span>", "")

                        // ------replace error font--------------
                        // grave -> " ` "
                        .replace("&agrave;", "à")
                        .replace("&Agrave;", "À")
                        .replace("&ograve;", "ò")
                        .replace("&igrave;", "ì")
                        .replace("&ygrave;", "ỳ")
                        .replace("&ugrave;", "ù")
                        .replace("&Ugrave;", "Ù")
                        .replace("&egrave;", "è")

                        // circ; -> " ^ "
                        .replace("&ecirc;", "ê")
                        .replace("&acirc;", "â")
                        .replace("&ocirc;", "ô")

                        // acute; -> " ' "
                        .replace("&aacute;", "á")
                        .replace("&Aacute;", "Á")
                        .replace("&oacute;", "ó")
                        .replace("&Oacute;", "Ó")
                        .replace("&iacute;", "í")
                        .replace("&yacute;", "ý")
                        .replace("&uacute;", "ú")
                        .replace("&eacute;", "é")
                        .replace("&Eacute;", "É")

                        // tilde; -> " ~ "
                        .replace("&atilde;", "ã")
                        .replace("&otilde;", "õ")
                        .replace("&itilde;", "ĩ")
                        .replace("&ytilde;", "ỹ")
                        .replace("&utilde;", "ũ")

                ct!!.positionTag = pos
                ct!!.content = chapterContent
                mOnLoadChapterResult!!.onLoadChapterSuccess(ct!!)
            } else {
                mOnLoadChapterResult!!.onLoadChapterFailure()
                this.cancel(true)
            }
        }
    }
}