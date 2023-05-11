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

        viewModel.transaction.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            viewModel.getSumAmountInc()
            viewModel.getSumAmountExp()
            updateTv()
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete)
            deleteAllTransactions()
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllTransactions() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Повне очищення")
        builder.setMessage("Ви точно хочете все видалити?")
        builder.setPositiveButton("Так") { _, _ ->
            viewModel.deleteAllTransactions()
            Toast.makeText(requireContext(), "Транзакції успішно видалені", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Ні") { _, _ -> }
        builder.create().show()
    }

    private fun updateTv() {
        viewModel.sumAmountExp.observe(viewLifecycleOwner) { expAmount ->
            viewModel.sumAmountInc.observe(viewLifecycleOwner) { incAmount ->
                viewModel.transaction.observe(viewLifecycleOwner) {
                    val balance = incAmount?.minus(expAmount?: 0.0) ?: 0.0
                    binding.expTV.text = expAmount.toString()
                    binding.incTV.text = incAmount.toString()
                    binding.balTV.text = balance.toString()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateTv()
    }

    override fun onStart() {
        super.onStart()
        updateTv()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
