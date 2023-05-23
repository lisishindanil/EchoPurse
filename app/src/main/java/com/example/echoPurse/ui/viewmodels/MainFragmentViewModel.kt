package com.example.echoPurse.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.echoPurse.repositories.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class MainFragmentViewModel(private val repo: TransactionRepository) : ViewModel() {

    val transaction = repo.transaction
    val sumAmountIncByDate = MutableLiveData(0.0)
    val sumAmountExpByDate = MutableLiveData(0.0)

    fun getSumAmountExpByDate(startDate: Date, endDate: Date) = viewModelScope.launch {
        val sumExpByDate = repo.getSumAmountExpByDateRange(startDate, endDate)
        sumAmountExpByDate.value = sumExpByDate
    }

    fun getSumAmountIncByDate(startDate: Date, endDate: Date) = viewModelScope.launch {
        val sumIncByDate = repo.getSumAmountIncByDateRange(startDate, endDate)
        sumAmountIncByDate.value = sumIncByDate
    }

    fun deleteTransaction(id: Long) = repo.deleteTransaction(id)

    fun getTransactionsByDateRange(startDate: Date, endDate: Date) = repo.getTransactionsByDateRange(startDate, endDate)

    class MainFragmentViewModelFactory(private val repo: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainFragmentViewModel(repo) as T
        }
    }
}