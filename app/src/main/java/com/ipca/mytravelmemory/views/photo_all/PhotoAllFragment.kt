package com.ipca.mytravelmemory.views.photo_all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.databinding.FragmentPhotoAllBinding
import com.ipca.mytravelmemory.models.PhotoModel

class PhotoAllFragment : Fragment() {
    private var _binding: FragmentPhotoAllBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoAllViewModel by viewModels()

    private var photos = arrayListOf<PhotoModel>()
    private val adapter = PhotosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // atualizar view com a lista das fotos

        binding.listViewPhotoAllPhotos.adapter = adapter

        // ao clicar no botão de adicionar foto. ir para o ecrã de abrir a câmara
        binding.buttonPhotoAllAddPhoto.setOnClickListener {
            findNavController().navigate(R.id.action__photoAll_to_photoCreate)
        }
    }

    inner class PhotosAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return photos.size
        }

        override fun getItem(position: Int): Any {
            return photos[position]
        }

        override fun getItemId(position: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_diary, parent, false)

            return rootView
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_PHOTO_CREATED = "EXTRA_PHOTO_CREATED"
    }
}