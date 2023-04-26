package com.example.rankinggames.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(
    @PrimaryKey (autoGenerate = true ) val id : Int = 0,
    @ColumnInfo (name = "name_group") val name: String,
    @ColumnInfo (name = "players_count") var playersCount: Int = 0
)


