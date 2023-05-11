package com.example.echoPurse.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.echoPurse.repositories.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragmentViewModel(private val repo: TransactionRepository) : ViewModel() {

    val transaction = repo.transaction
    val sumAmountExp = MutableLiveData<Double>(0.0)
    val sumAmountInc = MutableLiveData<Double>(0.0)

    fun getSumAmountExp() = viewModelScope.launch(Dispatchers.IO) {
        val sumExp = repo.getSumAmountExp()
        withContext(Dispatchers.Main) {
            sumAmountExp.value = sumExp
        }
    }
    fun getSumAmountInc() = viewModelScope.launch(Dispatchers.IO) {
        val sumInc = repo.getSumAmountInc()
        withContext(Dispatchers.Main) {
            sumAmountInc.value = sumInc
        }
    }
    fun deleteAllTransactions() = repo.deleteAllTransactions()
    fun deleteTransaction(id: Long) = repo.deleteTransaction(id)

    class MainFragmentViewModelFactory(private val repo: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainFragmentViewModel(repo) as T
        }
    }
}