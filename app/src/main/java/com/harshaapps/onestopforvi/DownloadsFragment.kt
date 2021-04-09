package com.harshaapps.onestopforvi

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harshaapps.onestopforvi.downloads.DownloadsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DownloadsFragment : Fragment() {
    private var columnCount = 1
private lateinit var view:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
setHasOptionsMenu(true)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_item_list, container, false) as RecyclerView
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val instance = DownloadsDatabase.getInstance(requireContext())
        GlobalScope.launch(Dispatchers.IO)
        {
            val softwares = instance.getDownloadedSoftware()
GlobalScope.launch(Dispatchers.Main)
{
    view.adapter = MyDownloadsRecyclerViewAdapter(softwares)
}
        }
            }

    override fun onPrepareOptionsMenu(menu: Menu) {
menu.clear()
        super.onPrepareOptionsMenu(menu)
    }



    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        fun newInstance(columnCount: Int) =
            DownloadsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}