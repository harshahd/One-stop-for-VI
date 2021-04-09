package com.harshaapps.onestopforvi

import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
private const val tag="FetchWebContents."
class FetchWebContents {
    private var respCode: Int = 0
    @Throws(IOException::class)
    fun getHtmlCode(url1: String): String {
        Log.d(tag, "Starting fethcing from the web")
        val url = URL(url1)
            val connection = url.openConnection() as HttpURLConnection
        Log.d(tag, "Connection opened")
                try {
            val out = ByteArrayOutputStream()
                                val inpStream = connection.inputStream
                    Log.d(tag, "Got inputstream. now lets continuously fetch. ${connection.responseCode}")
            respCode = connection.responseCode
            if (respCode != 200) {
                throw IOException()
            }
            var b = 0
            val buffer = ByteArray(1024)
                                                    while (true) {
                                                        b=inpStream.read(buffer)
                                                        Log.d(tag, "Reading... $b")
                                                        if (b>0) {
                                                            out.write(buffer, 0, b)
                                                            Log.d(tag, "Written")
                                                        }
                                                        else
                                                        {
                                                            Log.d(tag, "Writing completed")
                                                            break
                                                        }
                            }
                    Log.d(tag, "Fully fetch lets close connection.")
            out.close()
                    inpStream.close()
                   Log.d(tag, "Connection has been closed.")
            return String(out.toByteArray())
        } finally {
            connection.disconnect()
                    Log.d(tag, "Disconnected from the network.")
        }
    }
}

