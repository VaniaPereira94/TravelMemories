package com.ipca.mytravelmemory.views.diary_day_all

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.databinding.FragmentDiaryDayAllBinding
import com.ipca.mytravelmemory.models.DiaryDayModel

class DiaryDayAllFragment : Fragment() {
    private var _binding: FragmentDiaryDayAllBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiaryDayAllViewModel by viewModels()

    private var diaryDays = arrayListOf<DiaryDayModel>()
    private val adapter = DiaryDaysAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryDayAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // atualizar view com a lista dos dias do diário
        viewModel.getDiaryDaysFromFirebase().observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                diaryDays = it as ArrayList<DiaryDayModel>
                adapter.notifyDataSetChanged()
            }
            response.onFailure {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.listViewDiaryDayAllDiaryDays.adapter = adapter

        // ao clicar no botão de adicionar dia ao diário
        binding.buttonDiaryDayAllAddDiaryDay.setOnClickListener {
            // ir para a tela de adicionar dia ao diário
            findNavController().navigate(R.id.action_diaryDayAllFragment_to_diaryDayCreateFragment)
        }
    }

    inner class DiaryDaysAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return diaryDays.size
        }

        override fun getItem(position: Int): Any {
            return diaryDays[position]
        }

        override fun getItemId(position: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_diary, parent, false)

            val textViewName = rootView.findViewById<TextView>(R.id.textView_diaryAll_title)
            textViewName.text = diaryDays[position].title

            return rootView
        }
    }

    companion object {
        const val EXTRA_DIARY_DAY_CREATE = "EXTRA_DIARY_DAY_CREATE"
    }
}