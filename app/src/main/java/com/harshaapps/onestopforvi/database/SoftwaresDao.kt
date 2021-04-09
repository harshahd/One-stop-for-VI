package com.harshaapps.onestopforvi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface SoftwaresDao {
    @Query(value = "select * from softwares_list")
    fun getAllSoftwares():List<Software>
    @Insert
    fun addSoftware(software:Software)
@Query(value = "select count(*) from softwares_list")
fun getTotalSoftwares():Int
}
