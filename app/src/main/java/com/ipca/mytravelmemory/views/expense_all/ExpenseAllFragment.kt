package com.ipca.mytravelmemory.views.expense_all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.databinding.FragmentExpenseAllBinding
import com.ipca.mytravelmemory.models.ExpenseModel

class ExpenseAllFragment : Fragment() {
    private var _binding: FragmentExpenseAllBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpenseAllViewModel by viewModels()

    private var expenses = arrayListOf<ExpenseModel>()
    private val adapter = ExpensesAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentExpenseAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // atualizar view com a lista das despesas
        viewModel.getExpensesFromFirebase().observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                expenses = it as ArrayList<ExpenseModel>
                adapter.notifyDataSetChanged()
            }
            response.onFailure {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.listViewExpenseAllExpenses.adapter = adapter

        // ao clicar no botão de adicionar despesa
        binding.buttonExpenseAllAddExpense.setOnClickListener {
            // ir para a tela de adicionar despesa
            findNavController().navigate(R.id.action_expenseAll_to_expenseCreate)
        }
    }

    inner class ExpensesAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return expenses.size
        }

        override fun getItem(position: Int): Any {
            return expenses[position]
        }

        override fun getItemId(position: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_expense, parent, false)

            val textViewName = rootView.findViewById<TextView>(R.id.textView_expenseAll_title)
            textViewName.text = expenses[position].category

            return rootView
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_EXPENSE_CREATED = "EXTRA_EXPENSE_CREATED"
    }
}