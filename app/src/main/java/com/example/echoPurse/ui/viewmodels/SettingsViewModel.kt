package com.example.echoPurse.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.echoPurse.repositories.TransactionRepository

class SettingsViewModel(private val repo: TransactionRepository) : ViewModel() {

    fun deleteAllTransactions() = repo.deleteAllTransactions()

    class SettingsViewModelFactory(private val repo: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(repo) as T
        }
    }

}