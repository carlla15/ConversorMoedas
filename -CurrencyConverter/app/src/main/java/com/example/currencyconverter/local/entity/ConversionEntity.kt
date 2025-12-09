package com.example.currencyconverter.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.currencyconverter.data.local.database.Converters
// import com.example.currencyconverter.local.database.Converters
import java.util.Date

@Entity(tableName = "conversions")
@TypeConverters(Converters::class)
data class ConversionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fromCurrency: String,
    val toCurrency: String,
    val amount: Double,
    val convertedAmount: Double,
    val rate: Double,
    val timestamp: Date = Date(),
    val isDemo: Boolean = false
)