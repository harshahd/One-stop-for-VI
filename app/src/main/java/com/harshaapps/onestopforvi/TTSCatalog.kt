package com.harshaapps.onestopforvi

import android.Manifest
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.*
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.harshaapps.onestopforvi.downloads.DownloadedSoftware
import com.harshaapps.onestopforvi.downloads.DownloadsDatabase
import com.harshaapps.onestopforvi.downloads.HandleDownload
import com.harshaapps.onestopforvi.softwares.DownloadableSoftware
import com.harshaapps.onestopforvi.softwares.Software
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.HttpURLConnection
import java.net.URL

private const val tag="TTSCatalog"
class TTSCatalog : Fragment() {
    private lateinit var languageSpinner:Spinner
    private lateinit var engineSpinner:Spinner
    private lateinit var descriptionText:TextView
    private var description=""
    private var strLanguages = ArrayList<String>()
    private var nLanguages = ArrayList<String>()
    private var strEngine = ArrayList<String>()
    private var nEngine = ArrayList<String>()
    private var voices=ArrayList<Software>()
    private lateinit var voiceList:RecyclerView
    private var languageClick=false
    private var dropdownInitialized=false
    private lateinit var prevVoicePage:Button
    private lateinit var nextVoicePage:Button
    private var page=0
    private var engine="All"
    private var language="All"
    private var presentPosition=0
    private lateinit var ttsAdview1:AdView
    private lateinit var ttsAdd2:AdView
    private lateinit var downloader:DownloadManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
downloader=requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v=inflater.inflate(R.layout.fragment_t_t_s_catalog, container, false)
        languageSpinner=v.findViewById<Spinner>(R.id.select_language)
        engineSpinner=v.findViewById<Spinner>(R.id.select_tts)
        descriptionText=v.findViewById<TextView>(R.id.ttsDescription)
voiceList=v.findViewById(R.id.voiceList)
        prevVoicePage = v.findViewById<Button>(R.id.prevVoicePage)
        nextVoicePage = v.findViewById<Button>(R.id.nextVoicePage)
        voiceList.layoutManager=GridLayoutManager(requireActivity(), 2)
        voiceList.adapter=VoicesAdapter()
        ttsAdview1=v.findViewById<AdView>(R.id.ttsAdd1)
        ttsAdd2=v.findViewById<AdView>(R.id.ttsAdd2)
        MobileAds.initialize(activity)
        {

        }
        val adRequest1=AdRequest.Builder().build()
 val adRequest2=AdRequest.Builder().build()
        ttsAdview1.loadAd(adRequest1)
       ttsAdd2.loadAd(adRequest2)
showVoices("All", "All")
prevVoicePage.setOnClickListener {v ->
    if (page==0) {
        v.announceForAccessibility(getString(R.string.first_page))
        }
    else
    {
        page--
showVoices(engine, language)
        v.announceForAccessibility("Page "+(page+1))
    }
}
        nextVoicePage.setOnClickListener { v ->
            if (voices.size<10)
            {
                v.announceForAccessibility("You are at last page")
            }
            else {
                page++
showVoices(engine, language)
                v.announceForAccessibility("Page "+(page+1))
                            }
                                    }
        return v
    }

    override fun onStart() {
        super.onStart()
        languageSpinner.onItemSelectedListener= object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (presentPosition==position)
                {
                    return
                }
                else {
                    if (nLanguages.size > 0) {
                        language = nLanguages.get(position).toString()
                        Log.d(tag, nLanguages.get(position))
                        page = 0
showVoices("All", language)
                        presentPosition = position
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                if (languageClick)
                    languageClick=false
            }
        }


    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
            }

    private fun showVoices(engine:String, language:String)
    {
        GlobalScope.launch(Dispatchers.IO)
        {
            getVoices(engine, language)
            GlobalScope.launch(Dispatchers.Main)
            {
                if (voices.size>0)
                putVoices()
                else
                {
                    descriptionText.text=getString(R.string.server_down_connection_weak)
                    descriptionText.announceForAccessibility(getString(R.string.server_down_connection_weak))
                }
            }
        }
    }

    private suspend fun getVoices(engine:String, language:String)
    {
        Log.d(tag, language)
        voices.clear()
        var ttsURL = Uri.parse("https://www.blindhelp.net/software/tts").buildUpon().appendQueryParameter("engine", engine).appendQueryParameter("language", language).appendQueryParameter("page", page.toString()).build()
        val ttsPage=URL(ttsURL.toString()).readText()
        val document=Jsoup.parse(ttsPage)
        val htmlVoiceLists=document.getElementsByClass("item-list").get(0).getElementsByTag("ol").get(0).getElementsByTag("li")
        if (!dropdownInitialized) {
            val htmlEngineList = document.getElementById("edit-engine")
            val HtmlLanguageList = document.getElementById("edit-language")
            val htmlLanguages = HtmlLanguageList.getElementsByTag("option")
            val htmlEngines = htmlEngineList.getElementsByTag("option")
            for (language: Element in htmlLanguages) {
                strLanguages.add(language.text())
                nLanguages.add(language.attr("value"))
            }
            for (engine: Element in htmlEngines) {
                strEngine.add(engine.text())
                nEngine.add(engine.attr("value"))
            }
        }
        for (htmlVoice:Element in htmlVoiceLists)
        {
            var voiceLink=htmlVoice.getElementsByTag("a")
            var software = Software()
            software.title=voiceLink.get(0).text()
            Log.d(tag, software.title)
            software.address=voiceLink.get(0).attr("href")
            voices.add(software)
        }
        if (description.equals(""))
        {
            description=document.getElementsByClass("view-header").text()
        }
    }

    private suspend fun putVoices()
    {
        if (!dropdownInitialized) {
            var engineAdapter = ArrayAdapter(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                strEngine.toTypedArray()
            )
            var languageAdapter = ArrayAdapter(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                strLanguages.toTypedArray()
            )
            engineSpinner.adapter = engineAdapter
            languageSpinner.adapter = languageAdapter
            dropdownInitialized=true
        }
        descriptionText.text=description
        voiceList.adapter?.notifyDataSetChanged()
    }

    companion   object {
    fun newInstance() = TTSCatalog()
}

    inner class         VoicesAdapter: RecyclerView.Adapter<VoicesAdapter.VoiceHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceHolder {
            return VoiceHolder(layoutInflater.inflate(R.layout.list_item_software, parent, false))
        }

        override fun onBindViewHolder(holder: VoiceHolder, position: Int) {
            holder.bindSoftware(voices.get(position))
        }

        override fun getItemCount(): Int {
            return voices.size
        }

        inner class VoiceHolder(itemView:View): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener
        {
            private lateinit var softwareText:TextView
            private lateinit var softwareItem:Software
            private var authID=""
            private lateinit var request:DownloadManager.Request
            init {
                softwareText=itemView.findViewById<TextView>(R.id.software)
                softwareText.setOnClickListener(this)
                softwareText.setOnLongClickListener(this)
            }
            fun bindSoftware(software:Software)
            {
                softwareItem=software
                softwareText.text=software.title
GlobalScope.launch {
    softwareItem.address=setRedirectUrl(softwareItem.address).await()
}
            }
            private fun setRedirectUrl(address:String):Deferred<String>
            {
                var location =""
                return GlobalScope.async {
                        try {
                            val url = URL(address)
                            var connection = url.openConnection() as HttpURLConnection
                            connection.instanceFollowRedirects = false
                            connection.connect()
                            location= location+url.protocol + "://" + url.host
                            val path = connection.getHeaderField("location")
                            connection.headerFields.get("set-cookie")?.let {
                                authID =it.get(0)
                                Log.d("SoftwareActivity cookie", authID)
                            }
                            location= location+path
                            connection.disconnect()
                        }
                        catch (e:Exception)
                        {
                            Log.d("SoftwareActivity", "Problem in the sharepoint link")
                        }
                        location
                    }
                }


            override fun onClick(p0: View?) {
                var connected = false
                val cm = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val isInfoAvailable = cm.activeNetworkInfo!=null
                cm.activeNetworkInfo?.let {
                    connected=it.isConnected
                }
                if (!(isInfoAvailable and connected))
                {
                    Snackbar.make(p0!!, "Error. Connection not available or your connection is too slow. cant download file.", Snackbar.LENGTH_LONG).show()
                    return
                }
                              val fileName =                     URLUtil.guessFileName(softwareItem.address, null, null)
                if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        HandleDownload.downloadFile(
                            requireContext(),
                            softwareItem.address,
                            softwareItem.address,
                            fileName,
                            authID,
                            p0
                        )
                    } else {
                        requireActivity().requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 12)
                    }
                }
                else
                {
                    HandleDownload.downloadFile(
                        requireContext(),
                        softwareItem.address,
                        softwareItem.address,
                        fileName,
                        authID,
                        p0
                    )
                }
            }

            override fun onLongClick(v: View?): Boolean {
var clipBoardText=softwareItem.address
                val copyManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                var cd=ClipData.newUri(requireActivity().contentResolver, softwareItem.title, Uri.parse(softwareItem.address))
                copyManager.setPrimaryClip(cd)
                Snackbar.make(v!!, softwareItem.title+" link copied to the clipboard", Snackbar.LENGTH_LONG).show()
                return true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.isNotEmpty() and  (grantResults[0]==PackageManager.PERMISSION_GRANTED)))
        {
            Toast.makeText(requireContext(), R.string.can_download, Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(requireContext(), R.string.unable_download_permanent, Toast.LENGTH_LONG).show()
        }
        val granted = shouldShowRequestPermissionRationale(permissions.get(0))
        if (!granted)
        {
            Toast.makeText(requireContext(), R.string.unable_download_permanent, Toast.LENGTH_LONG).show()
            var i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            i.setData(Uri.fromParts("package", requireActivity().packageName, null))
            startActivity(i)
        }



    }

}



