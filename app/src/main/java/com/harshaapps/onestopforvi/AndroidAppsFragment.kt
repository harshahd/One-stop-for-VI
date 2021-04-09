package com.harshaapps.onestopforvi

import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.harshaapps.onestopforvi.database.SoftwareDatabase
import com.harshaapps.onestopforvi.softwares.SoftwareList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AndroidAppsFragment:SoftwaresFragment() {
    private lateinit var previousPage: Button
    private lateinit var nextPage:Button
    private lateinit var mAdView:AdView
    private lateinit var bottomAdd:AdView
        companion object
    {
        var pageNumber: Int = 0
        fun newInstance(): AndroidAppsFragment {
                        return AndroidAppsFragment()
        }
    }

    override    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                            val v = inflater.inflate(R.layout.fragment_android_apps, container, false)
                osCode = OSConstants.ANDROID
                pageNumber = 0
        mRecyclerView = v.findViewById(R.id.softwareAndroid)
                                                                    mRecyclerView.layoutManager=LinearLayoutManager(activity)
                previousPage = v.findViewById(R.id.prevAndroid)
        nextPage = v.findViewById(R.id.nextAndroid)
        category = v.findViewById<Spinner>(R.id.androidCategory)
        category.onItemSelectedListener =instance
        category.setSelection(presentPosition)
         mAdView=v.findViewById<AdView>(R.id.bannerAdd)
        bottomAdd=v.findViewById<AdView>(R.id.bottomBannerAdd)
                MobileAds.initialize(activity)
        {


        }
val adRequest:AdRequest = AdRequest.Builder().build()
        val bottomAdRequest=AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
bottomAdd.loadAd(bottomAdRequest)
        previousPage.setOnClickListener {
        if (pageNumber == 0)
                previousPage.announceForAccessibility(getString(R.string.first_page) as CharSequence)
else {
                pageNumber--
                previousPage.announceForAccessibility("page " + (pageNumber + 1))
                SoftwareListTask(requireActivity(),mRecyclerView, "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode&page=$pageNumber").execute()
            }

        }
        nextPage.setOnClickListener {
                pageNumber++
                nextPage.announceForAccessibility("Page " + (pageNumber + 1))
                SoftwareListTask(
                    requireActivity(),
                    mRecyclerView,
                    "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode&page=$pageNumber"
                ).execute()
            }
        SoftwareListTask(requireActivity(),mRecyclerView, "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode&page=$pageNumber").execute()
        return v
    }

}

