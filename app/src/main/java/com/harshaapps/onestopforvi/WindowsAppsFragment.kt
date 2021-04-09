package com.harshaapps.onestopforvi


import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Spinner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class WindowsAppsFragment:SoftwaresFragment() {
    private lateinit var previousPage:Button
    private lateinit var nextPage:Button
    private lateinit var mAdView:AdView
    private lateinit var bottomAdView:AdView
    companion object
    {
        var pageNumber: Int = 0
        fun newInstance(): WindowsAppsFragment {
            return WindowsAppsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_windows_apps, container, false)
        osCode = OSConstants.WINDOWS
        pageNumber = 0
                mRecyclerView = v.findViewById(R.id.softwareWindows)
        mRecyclerView.layoutManager=LinearLayoutManager(activity)
        previousPage = v.findViewById(R.id.prevWindows)
        nextPage = v.findViewById(R.id.nextWindows)
        category = v.findViewById<Spinner>(R.id.windowsCategory)
        category.onItemSelectedListener =instance
        category.setSelection(presentPosition)
        mAdView=v.findViewById<AdView>(R.id.bannerAdd2)
        bottomAdView=v.findViewById<AdView>(R.id.bottomBannerAdd2)
        MobileAds.initialize(activity)
        {


        }
                val adRequest: AdRequest = AdRequest.Builder().build()
        val bottomAdRequest = AdRequest.Builder().build()
                mAdView.loadAd(adRequest)
        bottomAdView.loadAd(bottomAdRequest)
        previousPage.setOnClickListener {
            if (pageNumber == 0)
                previousPage.announceForAccessibility(getString(R.string.first_page) as CharSequence)
            else {
                pageNumber--
                previousPage.announceForAccessibility("page " + (pageNumber + 1))
                SoftwareListTask(requireActivity(),mRecyclerView, "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode&page=${AndroidAppsFragment.pageNumber}").execute()
            }
                            }
        nextPage.setOnClickListener {
            pageNumber++
            nextPage.announceForAccessibility("Page " + (pageNumber + 1))
            SoftwareListTask(requireActivity(),mRecyclerView, "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode&page=${AndroidAppsFragment.pageNumber}").execute()
        }
        SoftwareListTask(requireActivity(),mRecyclerView, "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode&page=${AndroidAppsFragment.pageNumber}").execute()
        return v
    }
}

