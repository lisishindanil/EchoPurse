package com.example.echoPurse.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.echoPurse.model.Transaction
import com.example.echoPurse.room.TransactionDao
import java.util.*

class TransactionRepository(private val transactionDao: TransactionDao) {

    val transaction: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    fun addTransaction(transaction: Transaction) = Thread { transactionDao.insert(transaction) }.start()

    fun getSumAmountExp(): Double = transactionDao.getSumAmountExp()

    fun getSumAmountInc(): Double = transactionDao.getSumAmountInc()

    fun deleteAllTransactions() = Thread { transactionDao.deleteAllTransaction() }.start()

    fun deleteTransaction(id: Long) = Thread { transactionDao.deleteTransaction(id) }.start()

    fun getTransactionById(oldTransaction: MutableLiveData<Transaction>, id: Long) {
        Thread {
            transactionDao.getTransactionById(id).let {
                oldTransaction.postValue(it)
            }
        }.start()
    }

    fun updateTransaction(transaction: Transaction) = Thread { transactionDao.updateTransaction(transaction) }.start()
}