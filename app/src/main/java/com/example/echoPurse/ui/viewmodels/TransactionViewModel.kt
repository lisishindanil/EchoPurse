package com.example.echoPurse.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.echoPurse.model.Transaction
import com.example.echoPurse.repositories.TransactionRepository

class TransactionViewModel(private val repo: TransactionRepository) : ViewModel() {

    fun addTransaction(transaction: Transaction) = repo.addTransaction(transaction)
    fun getTransactionById(oldTransaction: MutableLiveData<Transaction>, id: Long) = repo.getTransactionById(oldTransaction, id)
    fun updateTransaction(transaction: Transaction) = repo.updateTransaction(transaction)

    class TransactionViewModelFactory(private val repo: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TransactionViewModel(repo) as T
        }
    }

}