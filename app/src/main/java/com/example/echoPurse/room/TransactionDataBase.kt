package com.example.echoPurse.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.echoPurse.model.Transaction
import com.example.echoPurse.room.converters.DateConverter

@Database(entities = [Transaction::class], version = 3)
@TypeConverters(DateConverter::class)
abstract class TransactionDataBase : RoomDatabase(){

    abstract fun transactionDao() : TransactionDao

    companion object {

        @Volatile
        private var INSTANCE : TransactionDataBase? = null

        fun getDatabase(context: Context): TransactionDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    TransactionDataBase::class.java, "transaction_db")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}