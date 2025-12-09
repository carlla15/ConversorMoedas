package com.example.currencyconverter.di

import android.content.Context
import com.example.currencyconverter.data.local.database.AppDatabase
import com.example.currencyconverter.data.repository.CurrencyRepository

object AppModule {
    private lateinit var appContext: Context
    private lateinit var database: AppDatabase
    private lateinit var repository: CurrencyRepository

    fun initialize(context: Context) {
        appContext = context.applicationContext
        database = AppDatabase.getDatabase(appContext)
        repository = CurrencyRepository(appContext, database)
    }

    fun getRepository(): CurrencyRepository {
        return repository
    }
}