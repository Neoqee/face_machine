package com.neoqee.facemachine.db

import android.content.Context
import androidx.room.Room
import com.neoqee.facemachine.MainActivity
import com.neoqee.facemachine.db.database.FaceDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object DbManager {

    private var faceDatabase: FaceDatabase? = null
    private val mutex = Mutex()
    private val faceDatabase2: FaceDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Room.databaseBuilder(MainActivity.mainContext!!.applicationContext,FaceDatabase::class.java,"face.db")
            .build()
    }

    fun initDatabase(context: Context){
        initFaceDatabase(context)
    }

    private fun initFaceDatabase(context: Context){
        GlobalScope.launch(Dispatchers.IO) {
            if (faceDatabase != null){
                return@launch
            }
            mutex.withLock {
                faceDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    FaceDatabase::class.java,
                    "face.db"
                )
                    .build()
            }
        }

    }

    fun getFaceDatabase() = faceDatabase


}