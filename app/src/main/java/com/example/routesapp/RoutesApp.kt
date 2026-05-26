package com.example.routesapp

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.routesapp.data.AuthApi
import com.example.routesapp.data.AuthInterceptor
import com.example.routesapp.data.AuthRepository
import com.example.routesapp.data.AuthStore
import com.example.routesapp.data.RoutesApi
import com.example.routesapp.data.RoutesRepository
import com.example.routesapp.data.SessionManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

val Context.authStore by preferencesDataStore("auth")

class RoutesApp: Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }

    class AppContainer(context: Context) {

        private val authStore = AuthStore(context.authStore)

        val sessionManager =
            SessionManager(authStore)

        private val BASE_URL = "http://192.168.1.2:8080/"
        //private const val BASE_URL = "http://routes.domagalski.it/"

        private val json = Json {
            ignoreUnknownKeys = true
        }

        private val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor { chain -> AuthInterceptor(sessionManager).intercept(chain) }
            .build()
        private val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()

        val routesApi: RoutesApi = retrofit.create(RoutesApi::class.java)
        val authApi: AuthApi = retrofit.create(AuthApi::class.java)

        val routesRepository: RoutesRepository = RoutesRepository(routesApi)
        val authRepository: AuthRepository = AuthRepository(authApi)
    }
}