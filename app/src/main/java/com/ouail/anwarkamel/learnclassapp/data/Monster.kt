package com.ouail.anwarkamel.learnclassapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ouail.anwarkamel.learnclassapp.IMAGE_IMAGE_URL
import com.squareup.moshi.Json


@Entity(tableName = "monsters")
data class Monster (
    @PrimaryKey(autoGenerate = true)
    val monsterId: Int,
  //  @Json(name = "monsterName")
    val monsterName: String,
    val imageFile: String,
    val caption: String,
    val description: String,
    val price: Double,
    val scariness: Int
) {
    val imageUrl
        get() = "$IMAGE_IMAGE_URL/$imageFile.webp"

    val thumbnailUrl
        get() = "$IMAGE_IMAGE_URL/${imageFile}_tn.webp"


}

