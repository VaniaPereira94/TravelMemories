package com.ipca.mytravelmemory.views.trip_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.databinding.FragmentTripDetailBinding
import com.ipca.mytravelmemory.models.TripModel

class TripDetailFragment : Fragment() {
    private var _binding: FragmentTripDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TripDetailViewModel by viewModels()

    private lateinit var trip: TripModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            trip = it.getSerializable(EXTRA_TRIP_DETAILS) as TripModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTripDetailCountry.text = trip.country
        binding.textViewTripDetailCities.text = trip.cities

        // ao clicar no botão de ir para o ecrã das fotos
        binding.buttonTripDetailPhotos.setOnClickListener {
            findNavController().navigate(R.id.action_tripDetail_to_photoAll)
        }

        // ao clicar no botão de ir para o ecrã das despesas
        binding.buttonTripDetailExpenses.setOnClickListener {
            findNavController().navigate(R.id.action_tripDetail_to_expenseAll)
        }

        // ao clicar no botão de ir para o ecrã do diário
        binding.buttonTripDetailDiary.setOnClickListener {
            findNavController().navigate(R.id.action_tripDetail_to_diaryDayAll)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_TRIP_DETAILS = "EXTRA_TRIP_DETAILS"
    }
}