package com.example.echoPurse.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.echoPurse.R
import com.example.echoPurse.model.Transaction
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(
    private val inflater: LayoutInflater,
    private val onClick: (Transaction) -> Unit
) :
    ListAdapter<Transaction, TransactionAdapter.ViewHolder>(UserDiffCallback) {

    class ViewHolder(
        view: View,
        val onClick: (Transaction) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val category = view.findViewById<TextView>(R.id.categoryTv)
        private val amount = view.findViewById<TextView>(R.id.amountTV)
        private val date = view.findViewById<TextView>(R.id.dateTv)
        private val type = view.findViewById<TextView>(R.id.typeTV)
        private var transaction: Transaction? = null

        init {
            view.setOnClickListener {
                transaction?.let {
                    onClick(it)
                }
            }
        }

        fun bind(transaction: Transaction) {
            this.category.text = transaction.category
            this.amount.text = transaction.amount.toString()
            this.type.text = transaction.type
            this.date.text = formatDate(transaction.created)
            this.transaction = transaction
        }

        private fun formatDate(date: Date): String {
            val dateFormat = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault())
            return dateFormat.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.transaction_item, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }

    object UserDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(
            oldItem: Transaction,
            newItem: Transaction
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Transaction,
            newItem: Transaction
        ) = oldItem.category == newItem.category &&
                oldItem.amount == newItem.amount &&
                oldItem.created == newItem.created &&
                oldItem.type == newItem.type
    }

}
