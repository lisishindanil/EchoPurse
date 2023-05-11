package com.example.echoPurse.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.echoPurse.repositories.TransactionRepository

class MainViewModel(private val repo: TransactionRepository) : ViewModel() {

    val transaction = repo.transaction

    class MainViewModelFactory(private val repo: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repo) as T
        }
    }

}