package com.example.echoPurse.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var type: String = "",
    var category: String = "",
    var amount: Double,
    var description: String = "",
    var created: Date = Date(),
    var updated: Date = Date()
)
