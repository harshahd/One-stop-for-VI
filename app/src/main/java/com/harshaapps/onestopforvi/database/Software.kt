package com.harshaapps.onestopforvi.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.NumberFormatException

@Entity(tableName = "softwares_list")
 class Software(@PrimaryKey
                @ColumnInfo(name = "software_url")
                val softwareURL:String,
                @ColumnInfo(name = "software_name")
@NonNull
 val softwareName:String) {

}