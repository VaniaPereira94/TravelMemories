package com.ipca.mytravelmemory.models

class UserModel {
    private var name: String
    private var country: String? = null

    constructor(name: String, country: String?) {
        this.name = name
        this.country = country
    }

    fun convertToHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "name" to name,
            "country" to country
        )
    }
}