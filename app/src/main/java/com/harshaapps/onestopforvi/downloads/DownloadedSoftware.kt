package com.harshaapps.onestopforvi.downloads

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = Downloads.TABLE)
data class DownloadedSoftware(@PrimaryKey
@ColumnInfo(name = Downloads.COLUMN_ID)
val id:Long,
                              @ColumnInfo(name = Downloads.file_name)
                              val name:String,
@ColumnInfo(name = Downloads.LOCAL_URI)
val localAddress:String,
        @ColumnInfo(name = Downloads.HTTP_URI)
        val httpAddress:String,
                              @ColumnInfo(name = Downloads.DOWNLOAD_TYPE)
                              val type:String,
                              @ColumnInfo(name = Downloads.DOWNLOAD_STATUS)
                              val status:Int)
{


}