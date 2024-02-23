package com.example.zad22zad2var5

import androidx.room.*
//интерфейс для доступа к БД и ее методы
@Dao
interface HistoryDao {
    @Query("SELECT * FROM facts ORDER BY id DESC LIMIT 10")
    suspend fun getRecentFacts(): List<History>

    @Insert
    suspend fun insertFact(history: History)

    @Delete
    suspend fun deleteFact(history: History)

    @Query("DELETE FROM facts")
    suspend fun deleteAllFacts()

    @Update
    suspend fun updateFact(history: History)
}