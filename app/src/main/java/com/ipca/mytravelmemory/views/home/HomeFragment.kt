package com.ipca.mytravelmemory.views.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.databinding.FragmentHomeBinding
import com.ipca.mytravelmemory.models.TripModel
import com.ipca.mytravelmemory.views.auth.AuthActivity
import com.ipca.mytravelmemory.views.trip_detail.TripDetailFragment

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var trips = arrayListOf<TripModel>()
    private val adapter = TripsAdapter()

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

        // atualizar view com a lista das viagens
        viewModel.getTripsFromFirebase().observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                trips = it as ArrayList<TripModel>
                adapter.notifyDataSetChanged()
            }
            response.onFailure {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.listViewHomeTrips.adapter = adapter

        // ao clicar no botão de adicionar viagem, ir para a página de adicionar viagem
        binding.buttonHomeAddTrip.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_tripCreate)
        }

        // ao clicar no botão de terminar sessão, terminar sessão do utilizador e ir para a tela de autenticação
        binding.buttonHomeLogOut.setOnClickListener {
            viewModel.signOutFromFirebase()

            val intent = Intent(this@HomeFragment.requireContext(), AuthActivity::class.java)
            startActivity(intent)
            activity?.finish();
        }
    }

    inner class TripsAdapter : BaseAdapter() {
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

            val textViewCountry = rootView.findViewById<TextView>(R.id.textView_home_country)
            textViewCountry.text = trips[position].country

            // fazer download da foto e visualizá-la na imageView
            val imageViewPhoto = rootView.findViewById<ImageView>(R.id.imageView_home_cover)
            viewModel.getPhotoURI(trips[position].coverPath!!) { response ->
                response!!.onSuccess { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(imageViewPhoto)
                }
                response!!.onFailure {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }

            // ao clicar numa viagem, ir para a tela da viagem e enviar os dados dessa viagem atual
            rootView.setOnClickListener {
                shareTripID(trips[position].id!!)
                val bundle = Bundle()
                bundle.putSerializable(TripDetailFragment.EXTRA_TRIP_DETAILS, trips[position])
                findNavController().navigate(R.id.action_home_to_tripDetail, bundle)
            }

            return rootView
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun shareTripID(tripID: String) {
        val sharedPreference = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPreference.edit()) {
            putString(getString(R.string.shared_trip_id), tripID)
            apply()
        }
    }

    companion object {
        const val EXTRA_TRIP_CREATED = "EXTRA_TRIP_CREATED"
    }
}