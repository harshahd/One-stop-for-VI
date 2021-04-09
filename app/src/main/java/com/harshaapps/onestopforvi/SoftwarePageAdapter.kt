package com.harshaapps.onestopforvi

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SoftwarePageAdapter(private val mContext:Context, private val fm:FragmentManager):FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
                val softwaresPage = when(position)
{
0->AndroidAppsFragment.newInstance()
1->WindowsAppsFragment.newInstance()
                2->MacAppsFragment.newInstance()
                3->LynicsUnixAppsFragment.newInstance()
                else->AndroidAppsFragment.newInstance()
}
return softwaresPage
    }
    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val pageTitle = when(position)
        {
            0->mContext.getString(R.string.android)
            1->mContext.getString(R.string.windows)
            2->mContext.getString(R.string.mac)
            3->mContext.getString(R.string.lynics)
            else->mContext.getString(R.string.android)
        }
        return pageTitle
    }
}

