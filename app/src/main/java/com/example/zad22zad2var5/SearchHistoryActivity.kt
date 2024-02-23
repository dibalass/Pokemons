package com.example.zad22zad2var5

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchHistoryActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        //Инициализация базы данных
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "fact-database"
        ).build()


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter()
        recyclerView.adapter = adapter

        // загрузка истории фактов из базы данных и отображение их в RecyclerView
        GlobalScope.launch(Dispatchers.IO) {
            val histories = database.historyDao().getRecentFacts()
            withContext(Dispatchers.Main) {
                adapter.submitList(histories)
                Log.d(ContentValues.TAG, "Загружено ${histories.size} фактов из базы данных")
            }
        }

        adapter.setOnDeleteClickListener { fact ->
            GlobalScope.launch(Dispatchers.IO) {
                // Удаление выбранного факта из базы данных
                database.historyDao().deleteFact(fact)

                // Обновление списка фактов на экране
                val histories = database.historyDao().getRecentFacts()
                withContext(Dispatchers.Main) {
                    adapter.submitList(histories)
                }
            }
        }

        // редактирование факта
        adapter.setOnEditClickListener { fact ->
            showEditDialog(fact)
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun showEditDialog(fact: History) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        val text = fact.text
        val number = text.substringBefore(" - ").toIntOrNull()

        if (number != null) alertDialogBuilder.setTitle("Редактировать факт об числе: '$number'")
        else alertDialogBuilder.setTitle("Редактировать факт об числе")

        val editText = EditText(this)
        val textToEdit = text.substringAfter(" - ").trim()
        editText.setText(textToEdit)

        alertDialogBuilder.setView(editText)

        alertDialogBuilder.setPositiveButton("Сохранить") { _, _ ->
            val newText = editText.text.toString()
            val updateFact = fact.copy(text = "$number - $newText")
            GlobalScope.launch(Dispatchers.IO) {
                database.historyDao().updateFact(updateFact)
                val facts = database.historyDao().getRecentFacts()
                withContext(Dispatchers.Main) {
                    adapter.submitList(facts)
                }
            }
        }

        alertDialogBuilder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}