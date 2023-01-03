package com.ipca.mytravelmemory.views.trip_detail

import android.os.Bundle
import android.view.*
import android.widget.Toast
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

        // ao clicar no botão de apagar viagem
        binding.buttonTripDetailRemove.setOnClickListener {
            trip.coverPath?.let { coverPath ->
                viewModel.removeTripFromFirebase(trip.id!!, coverPath)
                    .observe(viewLifecycleOwner) { response ->
                        // ir para a página principal
                        response.onSuccess {
                            findNavController().navigate(R.id.fragment_navigationFooter_home)
                        }
                        response.onFailure {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
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