package com.ipca.mytravelmemory.views.diary_day_create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.databinding.FragmentDiaryDayCreateBinding
import com.ipca.mytravelmemory.models.DiaryDayModel
import com.ipca.mytravelmemory.utils.ParserUtil
import com.ipca.mytravelmemory.views.diary_day_all.DiaryDayAllFragment
import java.util.*

class DiaryDayCreateFragment : Fragment() {
    private var _binding: FragmentDiaryDayCreateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiaryDayCreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryDayCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dados do selecionador da data
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        """
        // ao clicar no botão de selecionar data do dia
        buttonStartDate.setOnClickListener {
            DatePickerDialog(
                this@,
                // ao selecionar data
                { view, y, m, d ->
                    textViewStartDate.text =  
                },
                year,
                month,
                day
            ).show()
        }
        """

        // ao clicar no botão de criar diário
        binding.buttonDiaryDayCreateSave.setOnClickListener {
            // definir viagem
            val diaryDay = DiaryDayModel(
                binding.editTextDiaryDayCreateTitle.text.toString(),
                binding.editTextDiaryDayCreateBody.text.toString(),
                ParserUtil.convertStringToDate("30-12-2022", "dd-MM-yyyy")
            )

            // ir para a tela do diário e enviar os dados do dia criado
            val bundle = Bundle()
            bundle.putSerializable(DiaryDayAllFragment.EXTRA_DIARY_DAY_CREATE, diaryDay)
            findNavController().navigate(R.id.action_diaryDayCreate_to_diaryDayAll, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}