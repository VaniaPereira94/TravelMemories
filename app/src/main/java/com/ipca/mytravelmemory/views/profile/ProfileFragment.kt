package com.ipca.mytravelmemory.views.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.databinding.FragmentProfileBinding
import com.ipca.mytravelmemory.models.UserModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var user: UserModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserFromFirebase().observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                user = it
                binding.editTextProfileCountry.setText(user.country)
                binding.editTextProfileName.setText(user.name)
                binding.editTextProfileEmail.setText(user.email)
            }
            response.onFailure {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        // ao clicar em atualizar dados
        binding.buttonProfileUpdateData.setOnClickListener {


            // ir para o ecrã principal
            findNavController().navigate(R.id.fragment_navigationFooter_home)
        }

        // ao clicar em atualizar email


        // ao clicar em atualizar senha

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}