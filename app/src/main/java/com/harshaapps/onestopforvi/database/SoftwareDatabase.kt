package com.harshaapps.onestopforvi.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Software::class), version = 1, exportSchema = false)
 abstract class SoftwareDatabase:RoomDatabase() {
    public  abstract fun softwareDao():SoftwaresDao
    companion object {
        private var instance: SoftwareDatabase? = null
        fun getInstance(context: Context): SoftwareDatabase {
            instance?.let {
                return it
            } ?: let {
                synchronized(this)
                {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        SoftwareDatabase::class.java,
                        "software_database"
                    ).build()
                    this.instance=instance
                    return instance
                }
            }
        }
    }

 fun getTotalSoftwares():Int
{
    var n=-1
    GlobalScope.launch(Dispatchers.IO)
    {
        instance?.let {
            n=softwareDao().getTotalSoftwares()
            Log.d("SoftwareEntity", softwareDao().getTotalSoftwares().toString())
        }
    }
    return n
}




}


