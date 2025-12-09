package com.example.currencyconverter.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.currencyconverter.data.local.entity.ConversionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversion(conversion: ConversionEntity): Long

    @Update
    suspend fun updateConversion(conversion: ConversionEntity)

    @Delete
    suspend fun deleteConversion(conversion: ConversionEntity)

    @Query("SELECT * FROM conversions ORDER BY timestamp DESC")
    fun getAllConversions(): Flow<List<ConversionEntity>>

    @Query("SELECT * FROM conversions WHERE id = :id")
    suspend fun getConversionById(id: Long): ConversionEntity?

    @Query("SELECT * FROM conversions WHERE fromCurrency = :from AND toCurrency = :to ORDER BY timestamp DESC")
    fun getConversionsByPair(from: String, to: String): Flow<List<ConversionEntity>>

    @Query("DELETE FROM conversions WHERE id = :id")
    suspend fun deleteConversionById(id: Long)

    @Query("DELETE FROM conversions")
    suspend fun clearAllConversions()

    @Query("SELECT COUNT(*) FROM conversions")
    suspend fun getConversionsCount(): Int
}