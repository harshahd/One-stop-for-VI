package com.harshaapps.onestopforvi

import android.app.DownloadManager
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.ContextMenu
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.harshaapps.onestopforvi.downloads.DownloadedSoftware
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.timer


class MyDownloadsRecyclerViewAdapter(
    private val values: List<DownloadedSoftware>
) : RecyclerView.Adapter<MyDownloadsRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
                        return ViewHolder(view,parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View, private var vg: ViewGroup) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        var contentView: TextView = view.findViewById(R.id.content)
        var downloadedBytes=0
val mContext = vg.context
        private lateinit var software: DownloadedSoftware
        private lateinit var query: DownloadManager.Query
        private lateinit var downloader: DownloadManager
        private var statusCode=-1
        private lateinit var c:Cursor
        private var downloadStatus=""
        private var byteString="MB"
        private var byteFloat = 0.00
        init {
            downloader=mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            query = DownloadManager.Query()
        }
                fun bind(software:DownloadedSoftware)
        {
            this.software=software
                  query.setFilterById(software.id)
              c =downloader.query(query)
            c.moveToFirst()
            downloadedBytes = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        statusCode = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            /*
            byteString = if (downloadedBytes<1000.0)
                {
                    byteFloat = downloadedBytes.toDouble()/1024.0
                                        "KB"
                }
                else if (downloadedBytes<1048000.0) {
                    byteFloat = (downloadedBytes.toFloat()/1024.0/1024.0)
                                        "MB"
                }
                else if (downloadedBytes>1098907000) {
                    byteFloat = downloadedBytes.toFloat()/1024.0/1024.0/1024.0
                    "GB"
                }
            else if (downloadedBytes>1125281431000) {
                    byteFloat = downloadedBytes.toDouble()/1024.0/1024.0/1024.0/1024.0
                    "TB"
                }
else {
                byteFloat = downloadedBytes.toDouble()
                "B"
            }

             */
            downloadStatus = when(statusCode)
            {
                DownloadManager.STATUS_SUCCESSFUL->"Completed"
                DownloadManager.STATUS_RUNNING->"Downloading"
                DownloadManager.STATUS_FAILED->"Download failed"
                DownloadManager.STATUS_PAUSED->"Download paused"
                DownloadManager.STATUS_PENDING->"Pending download"
                else->"Can't download"
            }
            byteFloat = (downloadedBytes.toFloat()/1024.0)/1024.0
            idView.text = downloadStatus
                                    contentView.text = "%.2f".format(byteFloat  )+byteString+", "+software.name
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}