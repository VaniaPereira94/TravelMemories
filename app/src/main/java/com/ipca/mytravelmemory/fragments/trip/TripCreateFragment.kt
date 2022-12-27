package com.ipca.mytravelmemory.fragments.trip

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.databinding.FragmentTripCreateBinding
import com.ipca.mytravelmemory.fragments.home.HomeFragment
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.services.AuthService
import com.ipca.mytravelmemory.services.TripService
import com.ipca.mytravelmemory.utils.ParserUtil
import java.util.*

class TripCreateFragment : Fragment() {
    private var _binding: FragmentTripCreateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TripViewModel by viewModels()

    private lateinit var authService: AuthService
    private lateinit var trip: TripModel

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
                DatePickerDialog.OnDateSetListener { view, y, m, d ->
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
                DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    binding.textViewTripCreateEndDate.text = "$d-${m + 1}-$y"
                },
                year,
                month,
                day
            ).show()
        }

        // ao clicar no botão de criar viagem
        binding.buttonTripCreateSave.setOnClickListener {
            // id do utilizador
            authService = AuthService()
            val userID = authService.getUserID()

            // definir viagem
            trip = TripModel(
                binding.editTextTripCreateCountry.text.toString(),
                binding.editTextTripCreateCities.text.toString(),
                ParserUtil.convertStringToDate(
                    binding.textViewTripCreateStartDate.text.toString(),
                    "dd-MM-yyyy"
                ),
                ParserUtil.convertStringToDate(
                    binding.textViewTripCreateEndDate.text.toString(),
                    "dd-MM-yyyy"
                )
            )

            """
            // criar viagem na base de dados
            tripService.create(userID, trip.convertToHashMap(), lifecycleScope) { isCreated ->
                if (!isCreated) {
                    Toast.makeText(context, "Erro ao criar viagem.", Toast.LENGTH_SHORT).show()
                    return@create
                }

                // ir para a tela das viagens e enviar os dados da viagem criada
                val bundle = Bundle()
                bundle.putSerializable(HomeFragment.EXTRA_TRIP_CREATE, trip)
                findNavController().navigate(R.id.action_tripCreateFragment_to_homeFragment, bundle)
            }
            """
        }
    }
}