plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id ("kotlin-kapt")

}
android {
    namespace = "com.example.zad22zad2var5"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.zad22zad2var5"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Зависимости для архитектуры и базы данных
    annotationProcessor ("androidx.room:room-compiler:2.6.0")
    implementation ("androidx.room:room-runtime:2.6.0")
    implementation ("androidx.room:room-ktx:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")

    // Зависимость для работы с JSON
    implementation ("com.google.code.gson:gson:2.9.0")

    // Зависимость для работы с корутинами
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Зависимость для сетевых запросов
    implementation ("com.android.volley:volley:1.2.1")

    // Зависимости для пользовательского интерфейса
    implementation ("com.google.android.material:material:1.10.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
}