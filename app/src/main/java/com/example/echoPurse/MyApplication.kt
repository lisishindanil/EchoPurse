package com.example.echoPurse

import android.app.Application
import com.example.echoPurse.repositories.TransactionRepository
import com.example.echoPurse.room.TransactionDataBase


class MyApplication: Application() {

    private val database by lazy {
        TransactionDataBase.getDatabase(this)
    }

    val repository by lazy {
        TransactionRepository(database.transactionDao())
    }

}