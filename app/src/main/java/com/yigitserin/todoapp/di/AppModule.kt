package com.yigitserin.todoapp.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yigitserin.todoapp.data.local.AppPreferences
import com.yigitserin.todoapp.data.local.NoteDao
import com.yigitserin.todoapp.data.local.NoteDatabase
import com.yigitserin.todoapp.data.remote.LoginAPI
import com.yigitserin.todoapp.data.repository.DefaultListRepository
import com.yigitserin.todoapp.data.repository.DefaultLoginRepository
import com.yigitserin.todoapp.data.repository.ListRepository
import com.yigitserin.todoapp.data.repository.LoginRepository
import com.yigitserin.todoapp.service.AlarmScheduler
import com.yigitserin.todoapp.service.NotificationService
import com.yigitserin.todoapp.utils.Validator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNotificationService(@ApplicationContext context: Context) = NotificationService(context)

    @Singleton
    @Provides
    fun provideAlarmScheduler(@ApplicationContext context: Context) = AlarmScheduler(context)

    @Singleton
    @Provides
    fun provideDefaultListRepository(noteDao: NoteDao, validator: Validator) = DefaultListRepository(noteDao, validator) as ListRepository

    @Singleton
    @Provides
    fun provideNoteDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, NoteDatabase::class.java, NoteDatabase.DB_NAME).addMigrations(NoteDatabase.MIGRATION_1_2).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase) = noteDatabase.noteDao()

    @Singleton
    @Provides
    fun provideAppPreferences(@ApplicationContext context: Context) = AppPreferences(context)

    @Singleton
    @Provides
    fun provideDefaultLoginRepository(loginAPI: LoginAPI, validator: Validator, appPreferences: AppPreferences) = DefaultLoginRepository(loginAPI, validator, appPreferences) as LoginRepository

    @Singleton
    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginAPI = retrofit.create(LoginAPI::class.java)

    @Singleton
    @Provides
    fun provideKotlinJsonAdapterFactory(): KotlinJsonAdapterFactory = KotlinJsonAdapterFactory()

    @Singleton
    @Provides
    fun provideMoshi(kotlinJsonAdapterFactory: KotlinJsonAdapterFactory): Moshi = Moshi.Builder().add(kotlinJsonAdapterFactory).build()

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofitClient(okHttp: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit = Retrofit.Builder().addConverterFactory(moshiConverterFactory).client(okHttp).baseUrl(LoginAPI.BASE_URL).build()
}