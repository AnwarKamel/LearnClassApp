package com.ouail.anwarkamel.learnclassapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.ouail.anwarkamel.learnclassapp.LOG_TAG
import com.ouail.anwarkamel.learnclassapp.data.MonsterRepository


class MainViewModel(app: Application) : AndroidViewModel(app){

    private val dataRepo = MonsterRepository(app)
    val monsterData = dataRepo.monsterData





}