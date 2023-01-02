package com.ipca.mytravelmemory.views.photo_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ipca.mytravelmemory.databinding.FragmentPhotoDetailBinding
import com.ipca.mytravelmemory.models.PhotoModel

class PhotoDetailFragment : Fragment() {
    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoDetailViewModel by viewModels()

    private lateinit var photo: PhotoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            photo = it.getSerializable(EXTRA_PHOTO_DETAILS) as PhotoModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewPhotoDetailDescription.text = photo.description

        // fazer download da foto e visualizá-la na imageView
        photo.filePath?.let { filePath ->
            viewModel.getPhotoURI(filePath) { response ->
                response!!.onSuccess { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(binding.imageViewPhotoDetailPhoto)
                }
                response!!.onFailure {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_PHOTO_DETAILS = "EXTRA_PHOTO_DETAILS"
    }
}