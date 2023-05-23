package com.example.echoPurse.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.echoPurse.MyApplication
import com.example.echoPurse.R
import com.example.echoPurse.databinding.FragmentSettingsBinding
import com.example.echoPurse.ui.viewmodels.SettingsViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.SettingsViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteAllBtn.setOnClickListener {
            deleteAllTransactions()
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}