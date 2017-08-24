package com.theanhdev97.truyenchu.Model.Callback

import com.theanhdev97.truyenchu.Model.Chapter

/**
 * Created by DELL on 29/07/2017.
 */
interface OnLoadChapterResult {
    fun onLoadChapterSuccess(chapter: Chapter)

    fun onLoadChapterFailure()
}