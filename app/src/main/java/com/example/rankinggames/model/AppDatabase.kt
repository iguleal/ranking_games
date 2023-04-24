package com.example.rankinggames.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Group::class, Player::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun groupDao(): GroupDao
    abstract fun playerDao() : PlayerDao

    companion object{

        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            return if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "ranking_games"
                    ).build()
                }
                INSTANCE as AppDatabase
            } else {
                INSTANCE as AppDatabase
            }
        }
    }
}