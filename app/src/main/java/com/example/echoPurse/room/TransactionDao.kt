package com.example.echoPurse.room

import androidx.lifecycle.LiveData
import com.example.echoPurse.model.Transaction
import androidx.room.*
import java.util.*

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY updated DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: Long): Transaction

    @Update
    fun updateTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions")
    fun deleteAllTransaction()

    @Query("DELETE FROM transactions WHERE id = :id")
    fun deleteTransaction(id: Long)

    @Query("SELECT * FROM transactions WHERE created BETWEEN :startDate AND :endDate")
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): List<Transaction>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Дохід' AND created >= :startDate AND created <= :endDate")
    fun getSumAmountIncByDateRange(startDate: Date, endDate: Date): Double

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Витрата' AND created >= :startDate AND created <= :endDate")
    fun getSumAmountExpByDateRange(startDate: Date, endDate: Date): Double

}