package com.example.rankinggames.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true ) val id : Int = 0,
    @ColumnInfo(name = "name_player") val name: String,
    @ColumnInfo(name = "wins") var wins: Int = 0,
    @ColumnInfo(name = "groupId") val groupId: Int
    )
