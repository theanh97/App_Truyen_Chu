package com.theanhdev97.truyenchu.Utils

import android.content.Context
import com.theanhdev97.truyenchu.Model.Chapter
import com.theanhdev97.truyenchu.Model.Truyen
import com.theanhdev97.truyenchu.Model.TruyenHolder
import com.theanhdev97.truyenhchu.Utils.Const

/**
 * Created by DELL on 04/08/2017.
 */
class TruyenUtils {
    companion object {
        fun isDownloadable(truyen: Truyen, size: Int): Boolean {
            if(truyen!!.listChapter!!.size ==0)
                return false
            if (!isEmptyChapter(truyen!!.listChapter!![0])
                    && !isEmptyChapter(truyen!!.listChapter!![size / 2])
                    && !isEmptyChapter(truyen!!.listChapter!![size - 1]))
                return false
            return true
        }

        fun updateTruyenFromInternalStorage(truyen: Truyen, context: Context) {
            var truyenDownloadedBefore = getDownLoadedTruyenByTenTruyen(truyen.title)
            if (truyenDownloadedBefore != null)
                if (truyenDownloadedBefore!!.listChapter != null &&
                        truyenDownloadedBefore!!.listChapter!!.size > 0) {
                    for (i in 0..truyenDownloadedBefore!!.listChapter!!.size - 1) {
                        truyen!!.listChapter!![i] = truyenDownloadedBefore!!.listChapter!![i]
                    }
                }
        }

        fun getListTruyenDownloaded(context: Context): ArrayList<Truyen>? {
            return ExternalStorage.getObject(context, Const.LIST_TRUYEN_DOWNLOADED) as
                    ArrayList<Truyen>?
        }

        fun saveReadingTruyen(context: Context,
                              truyen: Truyen) {

            var listTruyenReading = TruyenHolder.getInstance().getListReading()
            if (listTruyenReading == null) {
                listTruyenReading = ArrayList<Truyen>()
                listTruyenReading!!.add(truyen)
            } else {
                var isExist = false
                for (i in 0 until listTruyenReading.size) {
                    var item = listTruyenReading!![i]
                    if (item.title.equals(truyen.title)) {
                        isExist = true
                        listTruyenReading!![i] = truyen
                        break
                    }
                }
                if (isExist == false)
                    listTruyenReading.add(truyen)
            }

            // save
            TruyenHolder.getInstance().setListReading(listTruyenReading)
            ExternalStorage.saveObject(context, Const.LIST_TRUYEN_READING, listTruyenReading)
        }

        fun getListTruyenReading(context: Context): ArrayList<Truyen>? {
            return ExternalStorage.getObject(context, Const.LIST_TRUYEN_READING) as ArrayList<Truyen>?
        }

        fun getContinousChapterPosition(context: Context, truyenTitle: String): Int {
            var listTruyenReading = TruyenHolder.getInstance().getListReading()
            if (listTruyenReading != null && listTruyenReading!!.size > 0)
                for (item in listTruyenReading!!)
                    if (item.title.equals(truyenTitle))
                        return item.curChapterReadingPosition
            return 0
        }

        fun saveTruyenDownLoaded(context: Context, tenTruyen: String, truyen: Truyen) {
            var listTruyenDownloaded = TruyenHolder.getInstance().getListDownloaded()
            if (listTruyenDownloaded == null) {
                listTruyenDownloaded = ArrayList<Truyen>()
                listTruyenDownloaded.add(truyen)
            } else {
                var isExist = false
                for (i in listTruyenDownloaded) {
                    if (i.title.equals(tenTruyen)) {
                        isExist = true
                        // list chapters
                        i.listChapter!!.clear()
                        i.listChapter!!.addAll(truyen!!.listChapter!!)

                        i.imageTwo = truyen!!.imageTwo
                        i.rate = truyen!!.rate
                        i.link = truyen!!.link
                        i.status = truyen!!.status
                        i.description = truyen!!.description
                        i.maxChapter = truyen!!.maxChapter
                        i.imageOne = truyen!!.imageOne
                        i.author = truyen!!.author
                        i.type = truyen!!.type
                    }
                }
                if (isExist == false)
                    listTruyenDownloaded!!.add(truyen)
            }
            TruyenHolder.getInstance().setListDownloaded(listTruyenDownloaded)
            ExternalStorage.saveObject(context, Const.LIST_TRUYEN_DOWNLOADED, listTruyenDownloaded)
        }

        fun getDownLoadedTruyenByTenTruyen(tenTruyen: String): Truyen? {
            var listTruyenDownloaded = TruyenHolder.getInstance().getListDownloaded()
            if (listTruyenDownloaded != null)
                for (i in listTruyenDownloaded!!)
                    if (i.title.equals(tenTruyen)) {
                        return i.copy()
                    }
            return null
        }

        fun isEmptyChapter(chapter: Chapter): Boolean {
            if (chapter.content!!.length > 10)
                return false
            return true
        }

        fun getHoanThanhUrl(url: String): String {
            return "${url}hoan/"
        }

        fun getPageNumberUrl(url: String, position: Int): String {
            return "${url}trang-${position}/"
        }

        fun getSearchTruyenUrl(keyword: String, pageNumber: Int): String {
            var listKeyWords = keyword.trim()
                    .replace(Regex("\\s+"), "+")
            if (pageNumber == 1)
                return "http://truyenfull.vn/tim-kiem/?tukhoa=${listKeyWords}"
            else
                return "${keyword}&page=${pageNumber}"
        }
    }
}