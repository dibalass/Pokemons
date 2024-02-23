package com.example.zad22zad2var5

import androidx.room.Entity
import androidx.room.PrimaryKey
//сущность для сохранения запросов в БД
@Entity(tableName = "facts")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var text: String
)
