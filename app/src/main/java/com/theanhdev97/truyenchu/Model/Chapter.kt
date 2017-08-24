package com.theanhdev97.truyenchu.Model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by DELL on 10/07/2017.
 */
//data class Chapter(var link: String, var title: String, var chapter: String, var content: String)
//    : Serializable {
//    var positionTag = -1
//
//    override fun toString(): String {
//        return "Chương ${chapter} : ${title}\nlink : ${link}\nContent : ${content}"
//    }
//}
data class Chapter(var link: String,
                   var title: String,
                   var chapter: String,
                   var content: String,
                   var positionTag: Int = -1)
    : Parcelable, Serializable {


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(link)
        dest!!.writeString(title)
        dest!!.writeString(chapter)
        dest!!.writeString(content)
        dest!!.writeInt(positionTag)
    }

    override fun describeContents(): Int = 0

    override fun toString(): String =
            "Chương ${chapter} : ${title} ----- Content : ${content.subSequence(0,30)}"


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Chapter> = object : Parcelable.Creator<Chapter> {
            override fun createFromParcel(`in`: Parcel): Chapter {
                return Chapter(
                        `in`.readString(),
                        `in`.readString(),
                        `in`.readString(),
                        `in`.readString(),
                        `in`.readInt())
            }

            override fun newArray(size: Int): Array<Chapter?> {
                return arrayOfNulls<Chapter>(size)
            }
        }
    }
}