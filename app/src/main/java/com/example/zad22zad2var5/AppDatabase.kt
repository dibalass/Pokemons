package com.example.zad22zad2var5

import androidx.room.Database
import androidx.room.RoomDatabase
//БД использует сущность и интерфейс доступа
@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}