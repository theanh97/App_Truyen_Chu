package com.theanhdev97.truyenchu.Model.Callback

import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 28/07/2017.
 */
interface OnLoadTruyenInformationResult {
    fun onLoadTruyenInformationSuccess(truyen: Truyen)

    fun onLoadTruyenInformationFailure()
}