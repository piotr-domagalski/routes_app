package com.example.routesapp.data

import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token =
            (sessionManager.state.value as? SessionState.LoggedIn)
                ?.token

        val request = chain.request()
            .newBuilder()
            .apply {
                if (token != null) {
                    header(
                        "Authorization",
                        token
                    )
                }
            }
            .build()

        val response = chain.proceed(request)

        if (response.code == 401) {
            sessionManager.clearState()
        }

        return response
    }
}