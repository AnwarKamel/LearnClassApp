package com.ouail.anwarkamel.learnclassapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MonsterDao {

    @Query("SELECT * FROM monsters")
    fun getAll(): List<Monster>

    @Insert
    suspend fun insertMonster(monster: Monster)

    @Insert
    suspend fun insertMonsters(monsters: List<Monster>)

    @Query("DELETE FROM monsters")
    suspend fun deleteAll()

}