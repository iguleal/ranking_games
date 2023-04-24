package com.example.rankinggames.model

import androidx.room.*

@Dao
interface GroupDao {

    @Insert
    fun insertGroup(group: Group)

    @Query("SELECT * FROM `Group`")
    fun getGroups() : List<Group>

    @Update
    fun updateGroup(group: Group)

    @Delete
    fun deleteGroup(group: Group): Int





}