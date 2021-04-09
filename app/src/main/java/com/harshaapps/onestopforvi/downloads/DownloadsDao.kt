package com.harshaapps.onestopforvi.downloads

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DownloadsDao {
    @Insert
 fun InsertDownloadedSoftware(software:DownloadedSoftware)
    @Query(value = "select * from downloads_table order by id desc")
    fun getDownloadedSoftwares():List<DownloadedSoftware>



}