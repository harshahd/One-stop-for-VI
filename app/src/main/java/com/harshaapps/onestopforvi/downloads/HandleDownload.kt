package com.harshaapps.onestopforvi.downloads

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.harshaapps.onestopforvi.R
import com.harshaapps.onestopforvi.SoftwareActivity

class HandleDownload() {
    companion object
    {
        fun downloadFile(context:Context, url:String, originalUrl:String, fileName:String, authID:String, p0: View?)
        {
                        val downloader= context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            var request = DownloadManager.Request(Uri.parse(url))
            if (!authID.equals(""))
            request.addRequestHeader("cookie", authID)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                request.allowScanningByMediaScanner()
            request.setAllowedOverMetered(true)
            request.setTitle(fileName)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            request.setShowRunningNotification(true)
            val alertBuilder = AlertDialog.Builder(p0!!.context).apply {
                setTitle(R.string.download_file)
                setMessage("Do you want to download "+fileName+"?")
setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, which ->
    val id = downloader.enqueue(request)
    Snackbar.make(p0!!, "Started downloading "+fileName+".\nSee download progress in the notification bar.", Snackbar.LENGTH_LONG).show()
    val downloaded = DownloadedSoftware(id, fileName, Environment.DIRECTORY_DOWNLOADS, originalUrl, "None", 0)
    DownloadsDatabase.getInstance(context).insert(downloaded)
})
                setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, which ->
                                        Snackbar.make(p0!!, R.string.cancelled_download, Snackbar.LENGTH_LONG).show()
                })
                            }
val dialog = alertBuilder.create()
            dialog.show()
        }

    }


}

