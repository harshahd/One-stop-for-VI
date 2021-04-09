package com.harshaapps.onestopforvi.downloads

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.*
import javax.microedition.khronos.opengles.GL


@Database(entities = arrayOf(DownloadedSoftware::class), version = 1, exportSchema = false)
abstract class DownloadsDatabase:RoomDatabase() {
 abstract fun downloadsDao():DownloadsDao
    private  lateinit var softwares:List<DownloadedSoftware>
    companion object {
        private var instance: DownloadsDatabase? = null
        fun getInstance(context: Context): DownloadsDatabase {
            instance?.let {
                return it
            }
            synchronized(this)
                {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DownloadsDatabase::class.java,
                        Downloads.database
                    ).build()
                    this.instance=instance
                    return instance
                }
            }



                 class InsertTask():AsyncTask<DownloadedSoftware, Void, String>()
        {
            override fun doInBackground(vararg params: DownloadedSoftware?): String {
                params[0]?.let {
                    instance?.downloadsDao()?.InsertDownloadedSoftware(it)
                }
                return ""
            }

        }



    }
fun insert(software:DownloadedSoftware)
{
        InsertTask().execute(software)
}

    suspend fun getDownloadedSoftware():List<DownloadedSoftware>
    {
softwares=downloadsDao().getDownloadedSoftwares()
        return softwares
    }

}


