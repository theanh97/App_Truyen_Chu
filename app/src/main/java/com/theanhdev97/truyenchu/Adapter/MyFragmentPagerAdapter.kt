package com.theanhdev97.truyenchu.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by DELL on 09/08/2017.
 */
class MyFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var listFragments: ArrayList<Fragment>? = null
    var listTitle: ArrayList<String>? = null

    init {
        this.listFragments = ArrayList<Fragment>()
        this.listTitle = ArrayList<String>()
    }

    override fun getItem(position: Int): Fragment {
        return listFragments!![position]
    }

    override fun getCount(): Int {
        return listFragments!!.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        listFragments!!.add(fragment)
        listTitle!!.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return listTitle!![position]
    }

}