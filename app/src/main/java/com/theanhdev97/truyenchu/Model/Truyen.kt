package com.theanhdev97.truyenchu.Model

import android.os.Parcel
import android.os.Parcelable
import android.provider.SyncStateContract
import java.io.Console
import java.io.Serializable

/**
 * Created by DELL on 06/07/2017.
 */
//data class Truyen
//(var link: String, var title: String, var maxChapter: String, var imageOne: String, var author: String,
// var rate: Double, var description: String, var type: String, var status: String, var
// imageTwo: String, var listChapter: ArrayList<Chapter>?) : Serializable {
//
//    var curChapterReadingPosition: Int = -1
//
//    override fun toString(): String {
//        return "\nlink : ${link}\nTitle : ${title}\nMax Chapter : ${maxChapter}\nImage One : ${imageOne}" +
//                "\nImage Two : ${imageTwo}\nAuthor : ${author}\nRate : ${rate}\nDescription : ${description}" +
//                "\nType : ${type}\nStatus : ${status}"
//    }
//}

data class Truyen(var link: String,
                  var title: String,
                  var maxChapter: String,
                  var imageOne: String,
                  var author: String,
                  var rate: Double,
                  var description: String,
                  var type: String,
                  var status: String,
                  var imageTwo: String,
                  var listChapter: ArrayList<Chapter>?,
                  var curChapterReadingPosition: Int = -1) : Parcelable, Serializable {


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(link)
        dest!!.writeString(title)
        dest!!.writeString(maxChapter)
        dest!!.writeString(imageOne)
        dest!!.writeString(author)
        dest!!.writeDouble(rate!!)
        dest!!.writeString(description)
        dest!!.writeString(type)
        dest!!.writeString(status)
        dest!!.writeString(imageTwo)

        dest!!.writeTypedList(listChapter)
        dest!!.writeInt(curChapterReadingPosition)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Truyen> = object : Parcelable.Creator<Truyen> {
            override fun createFromParcel(`in`: Parcel): Truyen =
                    Truyen(`in`.readString(),
                            `in`.readString(),
                            `in`.readString(),
                            `in`.readString(),
                            `in`.readString(),
                            `in`.readDouble(),
                            `in`.readString(),
                            `in`.readString(),
                            `in`.readString(),
                            `in`.readString(),
                            `in`.createTypedArrayList(Chapter.CREATOR),
                            `in`.readInt())

            override fun newArray(size: Int): Array<Truyen?> {
                return arrayOfNulls<Truyen>(size)
            }
        }
    }
}