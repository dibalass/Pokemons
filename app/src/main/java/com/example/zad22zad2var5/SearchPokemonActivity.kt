package com.example.zad22zad2var5

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchPokemonActivity : AppCompatActivity() {
    private lateinit var editTextPokemon: EditText
    private lateinit var buttonSearch: Button
    private lateinit var buttonSearchHistory: Button
    private lateinit var textViewResult: TextView
    private lateinit var imageViewPokemon: ImageView

    private lateinit var database: AppDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        editTextPokemon = findViewById(R.id.searchPokemon)
        buttonSearch = findViewById(R.id.button_search)
        buttonSearchHistory = findViewById(R.id.button_search_history)
        textViewResult = findViewById(R.id.result)
        imageViewPokemon = findViewById(R.id.imagePokemon)
        //нажатие кнопки поиска
        buttonSearch.setOnClickListener {
            val pokemonNameOrId = editTextPokemon.text.toString()
            searchPokemon(pokemonNameOrId)
            textViewResult.visibility = View.VISIBLE

        }
        //переход на экран с базой данных истории поиска
        buttonSearchHistory.setOnClickListener {
            val intent = Intent(this@SearchPokemonActivity, SearchHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun searchPokemon(nameOrId: String) {
        //Инициализация базы данных Room
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "fact-database"
        ).build()
        //Создание корутины для асинхронной работы
        GlobalScope.launch(Dispatchers.IO) {
            //Формирование URL-адреса для запроса к API покемонов
            val apiUrl = "https://pokeapi.co/api/v2/pokemon/$nameOrId"

            try {
                //Открытие соединения с API
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                //Получение кода ответа от сервера
                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //Инициализация потока чтения для ответа от сервера.
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    //Создание объекта StringBuilder для хранения ответа
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()
                    //Парсинг JSON-объекта из ответа.
                    val jsonObject = JSONObject(response.toString())
                    //Получение имени покемона из JSON-объекта.
                    val name = jsonObject.getString("name")
                    //Получение идентификатора покемона из JSON-объекта.
                    val id = jsonObject.getInt("id")
                    //Получение массива типов покемона из JSON-объекта.
                    val types = jsonObject.getJSONArray("types")
                    val typeList = mutableListOf<String>()
                    for (i in 0 until types.length()) {
                        val typeObj = types.getJSONObject(i)
                        //Получение имени типа покемона и добавление его в список
                        val typeName = typeObj.getJSONObject("type").getString("name")
                        typeList.add(typeName)
                    }
                    //Получение массива способностей покемона из JSON-объекта.
                    val abilities = jsonObject.getJSONArray("abilities")
                    val abilityList = mutableListOf<String>()
                    for (i in 0 until abilities.length()) {
                        val abilityObj = abilities.getJSONObject(i)
                        //Получение имени способности и добавление его в список
                        val abilityName = abilityObj.getJSONObject("ability").getString("name")
                        abilityList.add(abilityName)
                    }
                    //Получение URL-адреса для запроса информации о характеристиках покемона.
                    val speciesUrl = jsonObject.getJSONObject("species").getString("url")
                    val speciesInfo = fetchSpeciesInfo(speciesUrl)
                    //Получение URL-адреса изображения покемона.
                    val imageUrl = jsonObject.getJSONObject("sprites").getString("front_default")

                    runOnUiThread {
                        //Вывод информации
                        val resultText = "Имя: $name\nID: $id\nТипы: ${typeList.joinToString()}\nСпособности: ${abilityList.joinToString()}\nХарактеристики: $speciesInfo"
                        textViewResult.text = resultText
                        Picasso.get().load(imageUrl).into(imageViewPokemon)
                        //Сохранение запроса в базе данных
                        val history = History(text = resultText)
                        GlobalScope.launch(Dispatchers.IO) {
                            database.historyDao().insertFact(history)
                            Log.d(ContentValues.TAG, "Факт сохранен в базе данных: $history")
                        }
                    }
                } else {
                    runOnUiThread {
                        textViewResult.text = "Покемон не найден"
                    }
                }
                connection.disconnect()
            } catch (e: Exception) {
                runOnUiThread {
                    textViewResult.text = "Ошибка при выполнении запроса"
                }
            }
        }
    }
    //Функция для получения характеристик покемона
    private fun fetchSpeciesInfo(speciesUrl: String): String {
        val connection = URL(speciesUrl).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode

        return if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }

            reader.close()

            val jsonObject = JSONObject(response.toString())
            //Получение массива записей с текстом о характеристиках покемона из JSON-объекта
            val flavorTextEntries = jsonObject.getJSONArray("flavor_text_entries")

            var englishFlavorText = "Информация о характеристиках не найдена"

            for (i in 0 until flavorTextEntries.length()) {
                //Сохранение характеристик
                val entry = flavorTextEntries.getJSONObject(i)
                val language = entry.getJSONObject("language").getString("name")
                val flavorText = entry.getString("flavor_text")

                if (language == "en") {
                    englishFlavorText = flavorText
                    break
                }
            }
            englishFlavorText
        } else {
            "Информация о характеристиках не найдена"
        }
    }
}