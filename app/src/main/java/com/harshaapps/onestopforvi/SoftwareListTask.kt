package com.harshaapps.onestopforvi


import android.os.AsyncTask
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteConstraintException
import android.net.ConnectivityManager
import android.util.Log
import com.harshaapps.onestopforvi.database.SoftwareDatabase
import com.harshaapps.onestopforvi.parsers.SoftwareList
import com.harshaapps.onestopforvi.softwares.Software
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val tag="SoftwareListTask"
class SoftwareListTask(private val mContext:Context, private var mRecyclerView: RecyclerView, private var url:String):AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg voids: Void): Void? {
        try {
            Log.d(tag, "Listing task started")
                        val htmlCode = FetchWebContents().getHtmlCode(url)
            Log.d(tag, "Finished fetching html code. now, lets parse.")
                        SoftwareList().getSoftwares(htmlCode)
            Log.d(tag, "Parsing finished and stored in the list. finishing the background work")
/* val sList = com.harshaapps.onestopforvi.softwares.SoftwareList.softwares
GlobalScope.launch(Dispatchers.IO)
{
    for (software:Software in sList)
    {
        try {
            val sdb=com.harshaapps.onestopforvi.database.Software(software.address, software.title)
            SoftwareDatabase.getInstance(mContext).softwareDao().addSoftware(sdb)
        }
        catch (e:SQLiteConstraintException)
        {
            Log.d(tag, "Got duplicate URL. lets continue with next software")
            continue
        }
            }
    }

 */
        } catch (e: Exception) {
            Log.d(tag, "Something went wrong! ${e.printStackTrace()}")
                    }
        Log.d(tag, "exitting doInBackground.")
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        Log.d(tag, "Started main thread to list task. initializing connectivity manager")
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        Log.d(tag, "Retrieved connectivity manager")
                val connectionInfo = cm.activeNetworkInfo != null
        Log.d(tag, "Retrieved connectivity status")
        if (!connectionInfo || !cm.activeNetworkInfo!!.isConnected) {
            Log.d(tag, "Building an alert dialog")
            val ab:AlertDialog.Builder = AlertDialog.Builder(mContext).apply {
                setTitle(R.string.connection_error)
                    setMessage(R.string.connection_error_message)
                    setPositiveButton(R.string.retry, DialogInterface.OnClickListener { dialog, which ->
                                SoftwareListTask(mContext,mRecyclerView, url).execute()
                        })
                    setNegativeButton(R.string.cancel, null)
                }
            val dialog:AlertDialog = ab.create()
            Log.d(tag, "Creating successful")
dialog.show()
            Log.d(tag, "Alert has been shown to hte user")
           return
                            }
                val adapter = SoftwareListAdapter(mContext)
                Log.d(tag, "Adapter for listing initialized.")
        mRecyclerView.setAdapter(adapter)
        Log.d(tag, "Listing adapter has been set. finishing onPostExecute.")
    }
}

