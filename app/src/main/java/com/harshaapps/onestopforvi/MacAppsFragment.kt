package com.harshaapps.onestopforvi


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


class MacAppsFragment:SoftwaresFragment() {
    private lateinit var previousPage: Button
    private lateinit var nextPage: Button
    private lateinit var mAdView: AdView
    private lateinit var bottomAdView:AdView
    companion object {
        var pageNumber: Int = 0
        fun newInstance(): MacAppsFragment {
            return MacAppsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_mac_apps, container, false)
        osCode = OSConstants.MAC
        pageNumber = 0
        mRecyclerView = v.findViewById(R.id.softwareMac)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        previousPage = v.findViewById(R.id.prevMac)
        nextPage = v.findViewById(R.id.nextMac)
        category = v.findViewById<Spinner>(R.id.MacCategory)
        category.onItemSelectedListener =instance
        category.setSelection(presentPosition)
        MobileAds.initialize(activity)
        {


        }
        mAdView = v.findViewById(R.id.bannerAdd3)
        bottomAdView=v.findViewById(R.id.bottomBannerAdd3)
        val adRequest: AdRequest = AdRequest.Builder().build()
        val bottomAdRequest=AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
bottomAdView.loadAd(adRequest)
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

