package com.ouail.anwarkamel.learnclassapp.data

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.ouail.anwarkamel.learnclassapp.LOG_TAG
import com.ouail.anwarkamel.learnclassapp.WEB_SERVICE_URL
import com.ouail.anwarkamel.learnclassapp.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MonsterRepository(val app: Application) {

    val monsterData = MutableLiveData<List<Monster>>()
    private val monsterDao = MonsterDatabase.getDatabase(app).monsterDao()


    private val listType = Types.newParameterizedType(
        List::class.java, Monster::class.java
    )

    init {

        CoroutineScope(Dispatchers.IO).launch {
            val data = monsterDao.getAll()
            if (data.isEmpty()) {
                callWebService()
            } else {
                /*
                I'm working in a background Thread so I can't use the value attribute
                that's only supposed to be called in the foreground thread
                so instead I'Il call postValue
                 */
                monsterData.postValue(data)
                // in coroutine you can't call a toast directly,
                // that's because you're in a background thread and the toast architecture has to be called from a foreground thread

                withContext(Dispatchers.Main) {
                    Toast.makeText(app, "Using local data", Toast.LENGTH_LONG).show()
                }

            }
        }

        /*
        // if use read cache or text
        val data = readDataFromCache()
        if (data.isEmpty()) {
            refreshDataFromWeb()
        } else {
            monsterData.value = data
            Log.i(LOG_TAG, "Using Local data")
        }
        // refreshDataFromWeb()
        // Log.i(LOG_TAG, "Network Available ${networkAvailable()}")

         */
    }

    @WorkerThread
    suspend fun callWebService() {
        if (networkAvailable()) {

            withContext(Dispatchers.Main) {
                Toast.makeText(app, "Using remote data", Toast.LENGTH_LONG).show()
            }

            Log.i(LOG_TAG, "Calling Web service")
            val retrofit = Retrofit.Builder()
                .baseUrl(WEB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit.create(MonsterService::class.java)
            val serviceData = service.getMonsterData().body() ?: emptyList()

            monsterData.postValue(serviceData)

            monsterDao.deleteAll()
            monsterDao.insertMonsters(serviceData)

            // if you use cache data
           // saveDataToCache(serviceData)
        }
    }


    @Suppress("DEPRECATION")
    private fun networkAvailable(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }

    fun refreshDataFromWeb() {
        CoroutineScope(Dispatchers.IO).launch {
            callWebService()
        }
    }


    private fun saveDataToCache(monsterData: List<Monster>) {

        if (ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val moshi = Moshi.Builder().build()
            val listType = Types.newParameterizedType(List::class.java, Monster::class.java)
            val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listType)
            val json = adapter.toJson((monsterData))
            FileHelper.saveTextToFile(app, json)
        }
    }

    private fun readDataFromCache(): List<Monster> {
        val json = FileHelper.readTextFromFile(app) ?: return emptyList()

        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Monster::class.java)
        val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listType)

        return adapter.fromJson(json) ?: emptyList()
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