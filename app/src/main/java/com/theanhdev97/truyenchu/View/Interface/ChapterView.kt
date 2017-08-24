package com.theanhdev97.truyenchu.View.Interface

import android.support.design.widget.BottomSheetBehavior
import android.view.View
import com.theanhdev97.truyenchu.Model.Chapter
import com.theanhdev97.truyenchu.Model.ChapterSetting
import com.theanhdev97.truyenchu.Model.Truyen

/**
 * Created by DELL on 30/07/2017.
 */
interface ChapterView {
    fun initTransitionAnimation()

    fun initProgressDialog()

    fun updateNewChapter(newChapter : Chapter)

    fun reloadNewChapterUI(newChapter: Chapter)

    fun saveReadingChapterPosition()

    fun showViewError()

    fun setFullScreen(isFullScreen: Boolean)

    fun setEventListener()

    fun initChapterSetting()

    fun initChapterData()

    fun setBottomSheetingDialog()

    fun updateSettingDialog(chapterSetting: ChapterSetting)

    fun setCollapsingToolbar()

    fun showBottomSheetingDialog(isShow: Boolean, bottomSheetBehavior: BottomSheetBehavior<View>)

    fun setChapterUI(chapterSetting: ChapterSetting?)

    fun setFullScreenUI(isFullScreen: Boolean)

    fun setLightLevelUI(lightLevel: Int)

    fun setBackgroundColorUI(backgroundColor: Int)

    fun setTextSizeUI(textSize: Int)

    fun setTextFontUI(textFont: String)

    fun setTextLineSpacingUI(textLineSpacingSize: Int)

    fun backToListChapters()

    fun showLightSetting()

    fun showTextSetting()

    fun showMenuSetting()

    fun inscreaseTextSize()

    fun descreaseTextSize()

    fun inscreaseTextLineSpacingSize()

    fun descreaseTextLineSpacingSize()

    fun setLightLevel()

    fun setBackgroundColor(colorID: Int)

    fun setTextFont()

    fun hideAllBottomShettingDialog()

    fun checkHasNextChapter(): Chapter?

    fun checkHasPrevChapter(): Chapter?

    fun showNotHasNextChapter()

    fun showNotHasPrevChapter()
}
