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

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Витрата'")
    fun getSumAmountExp(): Double

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Дохід'")
    fun  getSumAmountInc(): Double

    @Query("SELECT * FROM transactions WHERE date(created) = date(:selectedDate) ORDER BY updated DESC")
    fun getTransactionsByDate(selectedDate: Date): LiveData<List<Transaction>>

    @Query("DELETE FROM transactions")
    fun deleteAllTransaction()

    @Query("DELETE FROM transactions WHERE id = :id")
    fun deleteTransaction(id: Long)
}