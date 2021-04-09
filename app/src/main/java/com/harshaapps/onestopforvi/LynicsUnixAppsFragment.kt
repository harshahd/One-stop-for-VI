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
import kotlinx.android.synthetic.main.fragment_lynics_unix_apps.*


class LynicsUnixAppsFragment:SoftwaresFragment() {
    private lateinit var previousPage: Button
    private lateinit var nextPage:Button
    private lateinit var mAdView:AdView
    private lateinit var bottomAdView:AdView
        companion object
    {
                var pageNumber: Int = 0
        fun newInstance(): LynicsUnixAppsFragment {
            return LynicsUnixAppsFragment()
        }}
                override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_lynics_unix_apps, container, false)
        osCode = OSConstants.LYNICS
        pageNumber = 0
        mRecyclerView = v.findViewById(R.id.softwareLynics)
                    mRecyclerView.layoutManager=LinearLayoutManager(activity)
        previousPage = v.findViewById(R.id.prevLynics)
        nextPage = v.findViewById(R.id.nextLynics)
                    category = v.findViewById<Spinner>(R.id.lynicsCategory)
                    category.onItemSelectedListener =instance
                    category.setSelection(presentPosition)

mAdView=v.findViewById<AdView>(R.id.bannerAdd4)
                    bottomAdView = v.findViewById<AdView>(R.id.bottomBannerAdd4)
                                        MobileAds.initialize(activity)
                    {


                    }
                    val adRequest: AdRequest = AdRequest.Builder().build()
                    val bottomAdRequest=AdRequest.Builder().build()
                    mAdView.loadAd(adRequest)
                    bottomAdView.loadAd(bottomAdRequest)

        previousPage.setOnClickListener {
            if (pageNumber == 0)
                previousPage.announceForAccessibility(getString(R.string.first_page) as CharSequence)
            else {
                pageNumber--
                previousPage.announceForAccessibility("page " + (pageNumber + 1))
                SoftwareListTask(requireActivity(),mRecyclerView, "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode&page=${AndroidAppsFragment.pageNumber}").execute()
            }}
        nextPage.setOnClickListener {
            pageNumber++
            nextPage.announceForAccessibility("Page " + (pageNumber + 1))
            SoftwareListTask(requireActivity(),mRecyclerView, "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode&page=${AndroidAppsFragment.pageNumber}").execute()
                    }
                    SoftwareListTask(requireActivity(),mRecyclerView, "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode&page=${AndroidAppsFragment.pageNumber}").execute()
        return v
    }


}

