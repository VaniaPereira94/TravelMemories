package com.ipca.mytravelmemory.fragments.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.activities.AuthActivity
import com.ipca.mytravelmemory.databinding.FragmentHomeBinding
import com.ipca.mytravelmemory.fragments.trip.TripDetailFragment
import com.ipca.mytravelmemory.fragments.trip.TripViewModel
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.services.AuthService

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TripViewModel by viewModels()

    private lateinit var authService: AuthService

    private var trips = arrayListOf<TripModel>()
    private val adapter = TipsAdapter()

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authService = AuthService()

        // atualizar view com a lista das viagens
        viewModel.getTrips().observe(viewLifecycleOwner) { data ->
            """
            if (data == null) {
                return@observe
            }
            """

            trips = data as ArrayList<TripModel>
            adapter.notifyDataSetChanged()
        }

        """
        tripService = TripService()
        tripService.getAll(authService.getUserID(), lifecycleScope) { data ->
            if (data == null) {
                return@getAll
            }

            // atualizar view
            trips = data
            adapter.notifyDataSetChanged()
        }
        """

        binding.listViewHomeTrips.adapter = adapter

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // quando uma viagem é criada com sucesso no ecrã de criar viagem
            if (it.resultCode == Activity.RESULT_OK) {
                val trip = it.data?.getSerializableExtra(EXTRA_TRIP_CREATE) as TripModel
                trips.add(trip)

                adapter.notifyDataSetChanged()
            }
        }

        // ao clicar no botão de adicionar viagem
        binding.buttonHomeAddTrip.setOnClickListener {
            // ir para a página de adicionar viagem
            findNavController().navigate(R.id.action_homeFragment_to_tripCreateFragment)
        }

        // ao clicar no botão de terminar sessão
        binding.buttonHomeLogOut.setOnClickListener {
            // terminar sessão do utilizador
            authService.signOut()

            // ir para a página de autenticação
            val intent = Intent(this@HomeFragment.requireContext(), AuthActivity::class.java)
            startActivity(intent)
            activity?.finish();
        }
    }

    inner class TipsAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return trips.size
        }

        override fun getItem(position: Int): Any {
            return trips[position]
        }

        override fun getItemId(position: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_trip, parent, false)

            val textViewName = rootView.findViewById<TextView>(R.id.textView_main_title)
            textViewName.text = trips[position].country

            // ao clicar numa viagem
            rootView.setOnClickListener {
                // ir para a tela da viagem e enviar os dados da viagem atual
                val bundle = Bundle()
                bundle.putSerializable(TripDetailFragment.EXTRA_TRIP_DETAIL, trips[position])
                findNavController().navigate(R.id.action_homeFragment_to_tripDetailFragment, bundle)
            }

            return rootView
        }
    }

    companion object {
        const val EXTRA_TRIP_CREATE = "EXTRA_TRIP_CREATE"
    }
}