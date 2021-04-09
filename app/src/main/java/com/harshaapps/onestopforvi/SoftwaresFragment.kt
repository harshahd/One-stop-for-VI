package com.harshaapps.onestopforvi


import android.content.Intent
import android.net.Uri
import androidx.core.view.MenuItemCompat.getActionView
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.RecyclerView

private const val tag="SoftwaresFragment"
abstract class SoftwaresFragment : Fragment(), SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {
    lateinit var search: SearchView
    lateinit var category:Spinner
    lateinit var categoryAdapter:ArrayAdapter<Int>
    open lateinit var mRecyclerView: RecyclerView
    open protected var searchText = ""
    open var osCode: Int = 0
    var presentPosition=0
    var categoryString=CategoryConstants.ANY
    var instance=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "Creating fragment")
        requireActivity().invalidateOptionsMenu()
        setHasOptionsMenu(true)
        val uri = requireActivity().intent.data
        searchText = uri?.getQueryParameter("title") ?: ""
        Log.d(tag, "Created fragment")
        categoryAdapter= ArrayAdapter<Int>(requireContext(), android.R.layout.simple_list_item_1, R.array.categories)
                    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
if (position==presentPosition)
    return
presentPosition=position
                SoftwareListTask(
            requireActivity(), mRecyclerView,
            "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode"
        ).execute()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(tag, "Creating options menu")
        inflater.inflate(R.menu.fragment_softwares, menu)
        search = menu.findItem(R.id.search).getActionView() as SearchView
        search.apply {
            queryHint = "Enter software name"
            setOnQueryTextListener(this@SoftwaresFragment)
        }
        Log.d(tag, "Options menu created")
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchText = query
        AndroidAppsFragment.pageNumber = 0
        WindowsAppsFragment.pageNumber = 0
        MacAppsFragment.pageNumber = 0
        LynicsUnixAppsFragment.pageNumber = 0
        search.clearFocus()
        SoftwareListTask(
            requireActivity(), mRecyclerView,
            "https://blindhelp.net/software?title=$searchText&field_category_tid=${CategoryConstants.categories[presentPosition]}&os=$osCode"
        ).execute()
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }



}

