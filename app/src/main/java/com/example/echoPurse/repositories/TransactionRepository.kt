package com.example.echoPurse.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echoPurse.model.Transaction
import com.example.echoPurse.room.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class TransactionRepository(private val transactionDao: TransactionDao) {

    val transaction: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    fun addTransaction(transaction: Transaction) = Thread { transactionDao.insert(transaction) }.start()

    fun deleteAllTransactions() = Thread { transactionDao.deleteAllTransaction() }.start()

    fun deleteTransaction(id: Long) = Thread { transactionDao.deleteTransaction(id) }.start()

    fun getTransactionById(oldTransaction: MutableLiveData<Transaction>, id: Long) {
        Thread {
            transactionDao.getTransactionById(id).let {
                oldTransaction.postValue(it)
            }
        }.start()
    }

    fun getTransactionsByDateRange(startDate: Date, endDate: Date): List<Transaction> = transactionDao.getTransactionsByDateRange(startDate, endDate)
    fun updateTransaction(transaction: Transaction) = Thread { transactionDao.updateTransaction(transaction) }.start()

    suspend fun getSumAmountExpByDateRange(startDate: Date, endDate: Date): Double {
        return withContext(Dispatchers.IO) {
            transactionDao.getSumAmountExpByDateRange(startDate, endDate)
        }
    }

    suspend fun getSumAmountIncByDateRange(startDate: Date, endDate: Date): Double {
        return withContext(Dispatchers.IO) {
            transactionDao.getSumAmountIncByDateRange(startDate, endDate)
        }
    }
}