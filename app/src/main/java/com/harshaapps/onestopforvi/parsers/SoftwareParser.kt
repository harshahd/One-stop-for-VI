package com.harshaapps.onestopforvi.parsers


import com.harshaapps.onestopforvi.softwares.DownloadableSoftware
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*
import kotlin.collections.ArrayList


class SoftwareParser {
    val softwares:ArrayList<DownloadableSoftware> = ArrayList()
      fun getDownloadableSoftwares(html: String): ArrayList<DownloadableSoftware> {
        val document = Jsoup.parse(html)
        val body = document.body()
        val div = body.getElementsByTag("a")
        DownloadableSoftware.description =
            body.getElementsByClass("field field-name-body field-type-text-with-summary field-label-hidden")[0].text()
        for (element in div) {
                        val word = element.text().toLowerCase(Locale.getDefault())
            val partialWord = document.title().toLowerCase().split(" ")
            if (word.contains("download") || word.contains("get ") || word.contains("click ") || word.contains("buy ") || word.startsWith("for ") || word.contains(partialWord.get(0)+" "+partialWord.get(1)))
             {
                val ds = DownloadableSoftware()
                ds.title=element.text()
                ds.address=element.attr("href")
                 ds.directAddress=element.attr("href")
                                softwares.add(ds)
            }
        }
        return softwares
    }
}

