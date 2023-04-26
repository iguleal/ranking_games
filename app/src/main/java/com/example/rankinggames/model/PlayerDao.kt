package com.example.rankinggames.model

import androidx.room.*

@Dao
interface PlayerDao {

    @Insert
    fun insertPlayer(player: Player)

    @Query("SELECT * FROM `Player` WHERE groupId = :groupId")
    fun getPlayerById(groupId: Int?): List<Player>

    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player): Int


}