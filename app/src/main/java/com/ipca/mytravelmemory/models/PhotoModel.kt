package com.ipca.mytravelmemory.models

import android.graphics.Bitmap
import java.io.Serializable
import java.util.*

class PhotoModel : Serializable {
    var id: String? = null
    var filePath: String? = null  // caminho do ficheiro no storage
    var description: String? = null

    constructor(id: String?, filePath: String?, description: String?) {
        this.id = id
        this.filePath = filePath
        this.description = description
    }

    // dados que serão armazenados no documento da base de dados pertencente à foto
    fun convertToHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "filePath" to filePath,
            "description" to description
        )
    }

    companion object {
        fun convertToPhotoModel(id: String, hashMap: MutableMap<String, Any>): PhotoModel {
            return PhotoModel(
                id,
                hashMap["filePath"] as String,
                hashMap["description"] as String?
            )
        }
    }
}