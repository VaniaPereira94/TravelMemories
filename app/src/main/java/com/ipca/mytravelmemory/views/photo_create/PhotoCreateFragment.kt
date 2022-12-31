package com.ipca.mytravelmemory.views.photo_create

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.ipca.mytravelmemory.databinding.FragmentPhotoCreateBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotoCreateFragment : Fragment() {
    private var _binding: FragmentPhotoCreateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoCreateViewModel by viewModels()

    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openCameraAndTakePhoto()

        // ao clicar em guardar foto
        binding.buttonPhotoCreateSave.setOnClickListener {
            uploadFile { }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // quando é tirada uma foto com sucesso apartir da câmara
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // mostrar imagem original
            BitmapFactory.decodeFile(currentPhotoPath).apply {
                binding.imageViewPhotoCreatePhoto.setImageBitmap(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openCameraAndTakePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()

                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.ipca.mytravelmemory.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun uploadFile(callback: (String?) -> Unit) {
        var storage = Firebase.storage
        val storageRef = storage.reference
        val file = Uri.fromFile(File(currentPhotoPath))

        var metadata = storageMetadata {
            contentType = "image/jpg"
        }

        val ref = storageRef.child("images/${file.lastPathSegment}")

        val uploadTask = ref.putFile(file)

        // Observe state change events such as progress, pause, and resume
        // You'll need to import com.google.firebase.storage.ktx.component1 and
        // com.google.firebase.storage.ktx.component2
        uploadTask.addOnProgressListener { task ->
            val progress = (100.0 * task.bytesTransferred) / task.totalByteCount
            Log.d(TAG, "Upload is $progress% done")
        }.addOnPausedListener {
            Log.d(TAG, "Upload is paused")
        }

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback.invoke(file.lastPathSegment)
            } else {
                callback.invoke(null)
            }
        }
    }

    companion object {
        const val REQUEST_TAKE_PHOTO = 1
    }
}