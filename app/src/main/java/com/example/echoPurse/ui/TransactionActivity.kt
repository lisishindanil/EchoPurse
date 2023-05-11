package com.example.echoPurse.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.echoPurse.MyApplication
import com.example.echoPurse.R
import com.example.echoPurse.databinding.TransactionActivityBinding
import com.example.echoPurse.model.Transaction
import com.example.echoPurse.ui.viewmodels.TransactionViewModel
import java.util.*

class TransactionActivity: AppCompatActivity() {

    private val viewModel: TransactionViewModel by viewModels() {
        TransactionViewModel.TransactionViewModelFactory((application as MyApplication).repository)
    }

    private var editMode = false
    private val oldTransaction = MutableLiveData<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = TransactionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getLongExtra("transaction_id", -1L)
        editMode = id != -1L

        if (editMode)
            viewModel.getTransactionById(oldTransaction, id)

        oldTransaction.observe(this) {
            it?.let {
                binding.amountEd.editText?.setText(it.amount.toString())
                binding.categoryEd.editText?.setText(it.category)
            } ?: throw java.lang.IllegalArgumentException("transaction_id is incorrect")
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.save.setOnClickListener {
            val transactionType = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.radioButtonExp -> "Витрата"
                R.id.radioButtonInc -> "Дохід"
                else -> return@setOnClickListener
            }

            val date = Date()

            val category = binding.categoryEd.editText?.text.toString()
            val amount = binding.amountEd.editText?.text.toString()
            val description = binding.descriptionEd.editText?.text.toString()

            if (category.isEmpty() || amount.isEmpty() || transactionType.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Ви залишили поле пустим!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (editMode) {
                oldTransaction.value?.let {
                    it.amount = binding.amountEd.editText?.text.toString().toDouble()
                    it.category = binding.categoryEd.editText?.text.toString()
                    it.type = transactionType
                    it.description = binding.descriptionEd.editText?.text.toString()
                    it.updated = date
                    viewModel.updateTransaction(it)
                }
            } else {
                val transaction = Transaction(
                    category = binding.categoryEd.editText?.text.toString(),
                    amount = binding.amountEd.editText?.text.toString().toDouble(),
                    type = transactionType,
                    description = binding.descriptionEd.editText?.text.toString(),
                    created = date,
                    updated = date
                )
                viewModel.addTransaction(transaction)
            }
            finish()
        }
    }
}