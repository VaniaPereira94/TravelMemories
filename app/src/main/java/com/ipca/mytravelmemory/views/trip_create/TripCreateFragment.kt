package com.ipca.mytravelmemory.views.trip_create

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.databinding.FragmentTripCreateBinding
import com.ipca.mytravelmemory.views.home.HomeFragment
import java.util.*

class TripCreateFragment : Fragment() {
    private var _binding: FragmentTripCreateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TripCreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dados dos selecionadores de datas
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // ao clicar no botão de selecionar data de início
        binding.buttonTripCreateStartDate.setOnClickListener {
            DatePickerDialog(
                this@TripCreateFragment.requireContext(),
                // ao selecionar data
                { view, y, m, d ->
                    binding.textViewTripCreateStartDate.text = "$d-${m + 1}-$y"
                },
                year,
                month,
                day
            ).show()
        }

        // ao clicar no botão de selecionar data de fim
        binding.buttonTripCreateEndDate.setOnClickListener {
            DatePickerDialog(
                this@TripCreateFragment.requireContext(),
                // ao selecionar data
                { view, y, m, d ->
                    binding.textViewTripCreateEndDate.text = "$d-${m + 1}-$y"
                },
                year,
                month,
                day
            ).show()
        }

        // ao clicar no botão de criar viagem
        binding.buttonTripCreateSave.setOnClickListener {
            val country = binding.editTextTripCreateCountry.text.toString()
            val cities = binding.editTextTripCreateCities.text.toString()
            val startDate = binding.textViewTripCreateStartDate.text.toString()
            val endDate = binding.textViewTripCreateEndDate.text.toString()

            // adicionar viagem na base de dados
            viewModel.addTripToFirebase(country, cities, startDate, endDate)
                .observe(viewLifecycleOwner) { response ->
                    // ir para a tela das viagens e enviar os dados da viagem criada
                    response.onSuccess {
                        val bundle = Bundle()
                        bundle.putSerializable(HomeFragment.EXTRA_TRIP_CREATED, it)
                        findNavController().navigate(
                            R.id.action_tripCreate_to_home,
                            bundle
                        )
                    }
                    // mostrar erro
                    response.onFailure {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        return@observe
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}