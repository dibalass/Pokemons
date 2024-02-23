package com.example.zad22zad2var5

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import java.util.Random


class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var imageView: ImageView
    private lateinit var randomNumbers: IntArray
    private lateinit var sharedPreferences: SharedPreferences
    private val random = Random()

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.photopokemonsrandom)
        usernameEditText = findViewById(R.id.login)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginBTN)
        registerButton = findViewById(R.id.registerBTN)

        //определение рандомного числа для вывода соответствующего покемона
        randomNumbers = resources.getIntArray(R.array.random_numbers)
        val randomIndex = random.nextInt(randomNumbers.size)
        val randomNumber = randomNumbers[randomIndex]
        val imageName = "photo$randomNumber"
        val imageResource = resources.getIdentifier(imageName, "drawable", packageName)
        if (imageResource != 0)
            imageView.setImageResource(imageResource)

        sharedPreferences = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
    }
    //Вход в аккаунт
    fun login(view: View) {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

    //загрузка данных сохраненного пользователя
        val savedUsername = sharedPreferences.getString("login", "")
        val savedPassword = sharedPreferences.getString("password", "")

        if (username.isEmpty())
            Snackbar.make(view, "Введите логин", Snackbar.LENGTH_SHORT).show()
        else {
            if (password.isEmpty())
                Snackbar.make(view, "Введите пароль", Snackbar.LENGTH_SHORT).show()
            else {
                if (savedUsername == username && savedPassword == password) {
                    val intent = Intent(this, SearchPokemonActivity::class.java)
                    startActivity(intent)
                    finish()
                } else
                    Snackbar.make(view, "Неверный логин или пароль. Возможно вы еще не зарегистрировались", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    //регистрация пользователя
    fun registration(view: View) {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isEmpty())
            Snackbar.make(view, "Введите логин", Snackbar.LENGTH_SHORT).show()
        else {
            if (password.isEmpty())
                Snackbar.make(view, "Введите пароль", Snackbar.LENGTH_SHORT).show()
            else{
                sharedPreferences.edit().putString("login", username).apply()
                sharedPreferences.edit().putString("password", password).apply()
                Snackbar.make(view, "Вы успешно зарегистрировались", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
