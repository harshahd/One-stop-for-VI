package com.harshaapps.onestopforvi

import android.Manifest
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.snackbar.Snackbar
import com.harshaapps.onestopforvi.downloads.DownloadedSoftware
import com.harshaapps.onestopforvi.downloads.DownloadsDatabase
import com.harshaapps.onestopforvi.downloads.HandleDownload
import com.harshaapps.onestopforvi.parsers.SoftwareParser
import com.harshaapps.onestopforvi.softwares.DownloadableSoftware
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class SoftwareActivity : AppCompatActivity() {
    private var url: String? = null
    private lateinit var topAd:AdView
    private lateinit var bottomAd:AdView
        init {
instance=this
            }
    companion object
    {
        private lateinit var instance:SoftwareActivity
        private lateinit var filteredSoftwares:ArrayList<DownloadableSoftware>
        private lateinit var downloadableSoftwares:RecyclerView
        private lateinit var softwareDescription:TextView
        class DownloadableSoftwareTask(private var url:String):
            AsyncTask<Void, Void, Void>() {
            private lateinit var softwares: ArrayList<DownloadableSoftware>
            override fun doInBackground(vararg voids: Void): Void? {
                try {
                    val html = FetchWebContents().getHtmlCode(url)
                     softwares = SoftwareParser().getDownloadableSoftwares(html)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return null
            }


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
    val cm = instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val connectionInfo = cm.activeNetworkInfo != null
                if (!(connectionInfo || !cm.activeNetworkInfo!!.isConnected)) {
                    val ab = AlertDialog.Builder(instance).apply {
                        setTitle(R.string.connection_error)
                        setMessage(R.string.connection_error_message)
                        setPositiveButton(R.string.retry, DialogInterface.OnClickListener { dialog, which ->
                                    DownloadableSoftwareTask(url).execute()
})
                        setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, which ->
                                                    })
                    }
                    val dialog = ab.create()
                    dialog.show()
                    return
                }
                filteredSoftwares= ArrayList<DownloadableSoftware>()
                var count = 1
                for (software: DownloadableSoftware in softwares) {
                    if (count == softwares.size) {
                        break
                    }
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(software.address))
                    if (i.resolveActivity(instance.packageManager)!= null) {
                            filteredSoftwares.add(software)
                            count++
                                         }
                }
                downloadableSoftwares.adapter=SoftwareAdapter()
                softwareDescription.text = DownloadableSoftware.description
            }
        }

        class SoftwareAdapter:RecyclerView.Adapter<SoftwareAdapter.SoftwareHolder>()
        {
            private val googlePlay="play.google.com"
            private val apple="apps.apple.com"
            private val microsoft="microsoft.com"
            private val windowsPhone="windowsphone.com"
private val other="/"
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoftwareHolder {
                return SoftwareHolder(instance.layoutInflater.inflate(R.layout.list_item_software, parent, false))
            }

            override fun onBindViewHolder(holder: SoftwareHolder, position: Int) {
                holder.bindSoftware(filteredSoftwares.get(position))
            }

            override fun getItemCount(): Int {
                return filteredSoftwares.size
            }


            inner class SoftwareHolder(itemView:View):RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener
            {
                private lateinit var softwareText:TextView
                private lateinit var softwareItem:DownloadableSoftware
                private var authID=""
                private var count:Int = 0
                private var isDownloadable=false
                                init {
                    softwareText=itemView.findViewById<TextView>(R.id.software)
                    softwareText.setOnClickListener(this)
                    softwareText.setOnLongClickListener(this)
                                    }
                fun bindSoftware(software:DownloadableSoftware)
                {
                    softwareItem=software
                    softwareText.text=software.title
                    if (softwareItem.address.contains(googlePlay) or softwareItem.address.contains(apple) or softwareItem.address.contains(microsoft) or softwareItem.address[softwareItem.address.length-1].equals(other))
                    {
return
                    }
                    GlobalScope.launch {
                        softwareItem.directAddress=setRedirectUrl(softwareItem.address).await()
                    }
                }
                private fun setRedirectUrl(address:String):Deferred<String>
                {
                    var location =""
                    return GlobalScope.async {
                        if (!address.contains("sharepoint.com", true)) {
                            isDownloadable=true
location=address
                        }
                        else {
                            isDownloadable=true
                            try {
                                val url = URL(address)
                                var connection = url.openConnection() as HttpURLConnection
                                connection.instanceFollowRedirects = false
                                connection.connect()
                                location= location+url.protocol + "://" + url.host
                                val path = connection.getHeaderField("location")
                                connection.headerFields.get("set-cookie")?.let {
                                 authID =it.get(0)
                                    if (authID.equals(""))
                                        isDownloadable=false
                                Log.d("SoftwareActivity cookie", authID)
                                }
                                                                                                location= location+path
                                connection.disconnect()
                            }
                            catch (e:Exception)
                            {
                                Log.d("SoftwareActivity", "Problem in the sharepoint link")
                            }
                        }
                        location
                    }
                }

                override fun onClick(p0: View?) {
                    var connected = false
                    val cm = instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val isInfoAvailable = cm.activeNetworkInfo!=null
                    cm.activeNetworkInfo?.let {
                        connected=it.isConnected
                    }
                    if (!(isInfoAvailable and connected))
                    {
                        Snackbar.make(p0!!, "Error. Connection not available or your connection is too slow. cant download file.", Snackbar.LENGTH_LONG).show()
                        return
                    }
                                        val fileName =                     URLUtil.guessFileName(softwareItem.directAddress, null, null)
                    if (fileName.endsWith(".bin", true))
                        isDownloadable=false
                    if (!isDownloadable)
                    {
                         val i: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(softwareItem.address))
                         instance.startActivity(i)
return
                                            }
                    if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (ContextCompat.checkSelfPermission(
                                instance.applicationContext,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            HandleDownload.downloadFile(
                                instance.applicationContext,
                                softwareItem.directAddress,
                                softwareItem.address,
                                fileName,
                                authID,
                                p0
                            )
                        } else {
                            instance.requestPermissions(
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                12
                            )
                        }
                    }
                    else
                    {
                        HandleDownload.downloadFile(
                            instance.applicationContext,
                            softwareItem.directAddress,
                            softwareItem.address,
                            fileName,
                            authID,
                            p0
                        )
                                            }
                }


                override fun onLongClick(v: View?): Boolean {
                    val copyManager = instance.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    var cd= ClipData.newUri(instance.contentResolver, softwareItem.title, Uri.parse(softwareItem.address))
                    copyManager.setPrimaryClip(cd)
                    Snackbar.make(v!!, softwareItem.title+" link copied to the clipboard", Snackbar.LENGTH_LONG).show()
                    return true
                }
            }
        }


    }

    override protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_software)
                url = intent.getStringExtra("extra_downloadable_software")
        val title = intent.getStringExtra("extra_downloadable_title")
        if (url != null)
            setTitle(title)
        else {
            val uri = intent.data
            url = uri!!.toString()
            if (uri.getQueryParameter("title") != null) {
                val i = Intent(this, SoftwaresActivity::class.java)
                i.data = uri
                startActivity(i)
                return
            }
            setTitle(R.string.download_software)
        }
topAd = findViewById<AdView>(R.id.downloadBanner)
bottomAd = findViewById(R.id.bottomDownloadBanner)
        downloadableSoftwares=findViewById<RecyclerView>(R.id.downloadList)
        downloadableSoftwares.layoutManager=LinearLayoutManager(this)
        softwareDescription=findViewById<TextView>(R.id.softwareDescription)
        val topRequest = AdRequest.Builder().build()
        val bottomRequest = AdRequest.Builder().build()
        topAd.loadAd(topRequest)
        bottomAd.loadAd(bottomRequest)
                DownloadableSoftwareTask(url!!).execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
                super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.softwares_context_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.share_software -> {
                startChooser("Share this software VIA", url, "url")
            }
            R.id.share_description -> {
                run {
                    if (softwareDescription==null || softwareDescription.text.toString().equals("")) {
                        Toast.makeText(this, "No software to share. sorry.", Toast.LENGTH_SHORT)
                            .show()
                        return true
                    }
                    startChooser("Share description VIA", softwareDescription.text.toString(), "plain")
                }
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    fun startChooser(title: String, content: String?, type: String) {
        val ib = ShareCompat.IntentBuilder.from(this)
        ib.setChooserTitle(title)
        ib.setText(content)
        ib.setType("text/$type")
        ib.startChooser()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.isNotEmpty() and  (grantResults[0]==PackageManager.PERMISSION_GRANTED)))
        {
Toast.makeText(applicationContext, R.string.can_download, Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(applicationContext, R.string.unable_download_temperary, Toast.LENGTH_LONG).show()
        }
        val granted = shouldShowRequestPermissionRationale(permissions.get(0))
        if (!granted)
        {
            Toast.makeText(applicationContext, R.string.unable_download_permanent, Toast.LENGTH_LONG).show()
var i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            i.setData(Uri.fromParts("package", packageName, null))
            startActivity(i)
        }

    }


}


