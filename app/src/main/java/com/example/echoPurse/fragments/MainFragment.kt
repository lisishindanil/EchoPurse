package com.example.echoPurse.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echoPurse.MyApplication
import com.example.echoPurse.R
import com.example.echoPurse.databinding.FragmentMainBinding
import com.example.echoPurse.ui.TransactionActivity
import com.example.echoPurse.ui.adapters.TransactionAdapter
import com.example.echoPurse.ui.viewmodels.MainFragmentViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by viewModels {
        MainFragmentViewModel.MainFragmentViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            val i = Intent(requireContext(), TransactionActivity::class.java)
            startActivity(i)
        }

        binding.list.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TransactionAdapter(layoutInflater) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Дані про транзакцію")
            builder.setMessage(
                "Cумa: ${it.amount}\n"
                        + "Категорія: ${it.category}\n"
                        + "Тип: ${it.type}\n"
                        + "Опис: ${it.description}"
            )
            builder.setNeutralButton("Редагувати") {_, _ ->
                val i = Intent(requireContext(), TransactionActivity::class.java)
                i.putExtra("transaction_id", it.id)
                startActivity(i)
            }
            builder.setPositiveButton("Видалити") { _, _ ->
                val builder2 = AlertDialog.Builder(requireContext())
                builder2.setTitle("Видалення транзакції")
                    .setMessage("Ви впевнені, що хочете видалити дану транзакцію")
                    .setPositiveButton("Так") { _, _ ->
                        viewModel.deleteTransaction(id = it.id)
                        Toast.makeText(requireContext(), "Транзакція успішно видалена", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Ні") { _, _ -> }
                    .create().show()
            }
            builder.setNegativeButton("Повернутися") { _, _ -> }
            builder.create().show()
        }
        binding.list.adapter = adapter

        fun showDatePicker() {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                val startDate = Date(selection.first ?: 0)
                val endDate = Date(selection.second ?: 0)

                CoroutineScope(Dispatchers.IO).launch {
                    val transactions = viewModel.getTransactionsByDateRange(startDate, endDate)

                    withContext(Dispatchers.Main) {
                        adapter.submitList(transactions)
                        viewModel.getSumAmountExpByDate(startDate, endDate)
                        viewModel.getSumAmountIncByDate(startDate, endDate)
                        updateTv()
                    }
                }
            }
            datePicker.show(parentFragmentManager, "datePicker")
        }

        binding.analyticBtn.setOnClickListener {
            showDatePicker()
        }

        viewModel.transaction.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            val (startDate, endDate) = getStartAndEndOfMonth()
            viewModel.getSumAmountIncByDate(startDate, endDate)
            viewModel.getSumAmountExpByDate(startDate, endDate)
            updateTv()
        }
    }

    private fun getStartAndEndOfMonth(): Pair<Date, Date> {
        val calendar = Calendar.getInstance()

        calendar.time = Date()

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfMonth = calendar.time

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.SECOND, -1)
        val endOfMonth = calendar.time

        return Pair(startOfMonth, endOfMonth)
    }

    private fun updateTv() {
        viewModel.sumAmountExpByDate.observe(viewLifecycleOwner) { expAmount ->
            viewModel.sumAmountIncByDate.observe(viewLifecycleOwner) { incAmount ->
                        val balance = (incAmount ?: 0.0) - (expAmount ?: 0.0)
                        binding.expTV.text = expAmount.toString()
                        binding.incTV.text = incAmount.toString()
                        binding.balTV.text = balance.toString()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}