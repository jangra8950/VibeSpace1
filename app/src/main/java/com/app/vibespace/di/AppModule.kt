package com.app.vibespace.di

import android.content.Context
import android.content.res.Resources
import com.app.vibespace.service.ApiHelper
import com.app.vibespace.service.ApiHelperImpl
import com.app.vibespace.service.ApiRequest
import com.app.vibespace.service.MyRepo
import com.app.vibespace.ui.registration.SignInActivity
import com.app.vibespace.util.ApiConstants
import com.app.vibespace.util.ApiConstants.IS_SLIDE_ALREADY
import com.app.vibespace.util.MyApp.Companion.profileData

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl():String{
        return ApiConstants.BASE_URL
    }


    @Singleton
    @Provides
    fun getRetrofitInstance(
        @ApplicationContext appContext: Context,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(loggingInterceptor)

        okHttpClient.addInterceptor(Interceptor { chain ->

            val request = chain.request().newBuilder()
                .addHeader("Authorization", profileData?.accessToken.toString()).build()

            val response = chain.proceed(request)
            val body = response.body
            val bodyString = body!!.string()
            val contentType = body.contentType()


            response.newBuilder().body(bodyString.toResponseBody(contentType))
                .build()
        })

        return okHttpClient.build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiRequest = retrofit.create(ApiRequest::class.java)

//    @Provides
//    @Singleton
//    fun provideApiRequest(url:String): ApiRequest {
//        return Retrofit.Builder()
//            .baseUrl(url)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiRequest::class.java)
//    }

    @Provides
    @Singleton
    fun provideApiHelper(api:ApiRequest): ApiHelper {
        return ApiHelperImpl(api)
    }

    @Provides
    @Singleton
    fun provideMyRepo(api:ApiHelper):MyRepo{
        return MyRepo(api)
    }

    @Singleton
    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpInterceptor
    }

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext appContext: Context): Resources = appContext.resources

}