package com.harshaapps.onestopforvi

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

private const val tag="SoftwareFragment"
class SoftwareFragment : Fragment() {
    private lateinit var softwares: TabLayout
    private lateinit var osPage: ViewPager
    private lateinit var spa:SoftwarePageAdapter
    private lateinit var tab1: TabLayout.Tab
    private lateinit var tab2: TabLayout.Tab
    private lateinit var tab3: TabLayout.Tab
    private lateinit var tab4: TabLayout.Tab
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v:View = inflater.inflate(R.layout.fragment_software, container, false)
        softwares=v.findViewById(R.id.softwares)
        tab1=softwares.newTab()
        tab2=softwares.newTab()
        tab3=softwares.newTab()
        tab4=softwares.newTab()
        softwares.addTab(tab1)
        softwares.addTab(tab2)
        softwares.addTab(tab3)
        softwares.addTab(tab4)
        val uri:Uri? = activity?.intent?.data
        Log.d(tag, "Got URI")
        osPage=v.findViewById(R.id.os_page)
        spa= SoftwarePageAdapter(requireActivity().applicationContext, requireActivity().supportFragmentManager)
        Log.d(tag, "Pager adapter initialized")
        osPage.adapter=spa
        softwares.setupWithViewPager(osPage)
        Log.d(tag, "Attached to the pager")
                return v
    }

    companion object {
                fun newInstance():SoftwareFragment = SoftwareFragment()
    }
}
