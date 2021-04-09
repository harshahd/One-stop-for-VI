package com.harshaapps.onestopforvi.parsers

import android.util.Log
import com.harshaapps.onestopforvi.database.SoftwareDatabase
import com.harshaapps.onestopforvi.softwares.Software
import com.harshaapps.onestopforvi.softwares.SoftwareList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
private const val tag="ListParser"
class SoftwareList {
    fun getSoftwares(html: String) {
                val document:Document = Jsoup.parse (html)
        Log.d(tag, "Got the document")
        val htmlSoftwares:Elements  = document . getElementsByClass ("ds-1col node node-software node-teaser view-mode-teaser clearfix")
        Log.d(tag, "Got all softwares ${htmlSoftwares.size}")
        val softwares:ArrayList<Software> =ArrayList()
        Log.d(tag, "Initialized array list")
        for (htmlSoftware:Element in htmlSoftwares)
        {
            val softwareItem:Element = htmlSoftware . getElementsByTag ("h2").get(0)
            val s:Software = Software()
            s.address= "https://www.blindhelp.net${softwareItem.getElementsByTag("a").get(0).attr("href")}"
                        s.title=softwareItem . text ()
            Log.d(tag, "${s.title}")
softwares.add(s)
            Log.d(tag, "Software added total: ${softwares.size}")
        }
                SoftwareList.softwares = softwares
        Log.d(tag, "Stored all softwares. total: ${SoftwareList.softwares.size}")
                    }
}


