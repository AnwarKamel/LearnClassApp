package com.ouail.anwarkamel.learnclassapp.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.ouail.anwarkamel.learnclassapp.LOG_TAG
import com.ouail.anwarkamel.learnclassapp.WEB_SERVICE_URL
import com.ouail.anwarkamel.learnclassapp.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MonsterRepository(val app: Application) {

    val monsterData = MutableLiveData<List<Monster>>()

    private val listType = Types.newParameterizedType(
        List::class.java, Monster::class.java
    )

    init {

        CoroutineScope(Dispatchers.IO).launch {
            callWebService()
        }
       // Log.i(LOG_TAG, "Network Available ${networkAvailable()}")
    }



    @WorkerThread
    suspend fun callWebService() {
        if (networkAvailable()) {
            val retrofit = Retrofit.Builder()
                .baseUrl(WEB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit.create(MonsterService::class.java)
            val serviceData = service.getMonsterData().body() ?: emptyList()

            monsterData.postValue(serviceData)

        }
    }


    @Suppress("DEPRECATION")
    private fun networkAvailable():Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }

}




/*

    fun getMonsterData() {
        val text = FileHelper.getTextFromAssets(app, "monster_data.json")
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val adapter: JsonAdapter<List<Monster>> =
            moshi.adapter(listType)
         monsterData.value = adapter.fromJson(text)?: emptyList()
    }


 */