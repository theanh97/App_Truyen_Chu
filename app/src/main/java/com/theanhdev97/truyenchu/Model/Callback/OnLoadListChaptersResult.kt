package com.theanhdev97.truyenchu.Model.Callback

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 29/07/2017.
 */
interface OnLoadListChaptersResult {
    fun onLoadListChapterSuccess(truyen: Truyen)

    fun onLoadListChapterFailure()
}