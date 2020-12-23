package com.ouail.anwarkamel.learnclassapp.data

import com.squareup.moshi.Json

data class Monster (
  //  @Json(name = "monsterName")
    val monsterName: String,
    val imageFile: String,
    val caption: String,
    val description: String,
    val price: Double,
    val scariness: Int
)

