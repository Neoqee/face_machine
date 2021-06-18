package com.neoqee.facemachine.baidu

import android.util.Log
import com.baidu.idl.main.facesdk.model.Feature
import java.util.ArrayList
import java.util.concurrent.Executors
import java.util.concurrent.Future

object BdFeature {
    private val es = Executors.newSingleThreadExecutor()
    private var future: Future<*>? = null

    private var mUserNum: Int? = null
    private var isInitSuccess = false

    private val newEs = Executors.newFixedThreadPool(4)

    private val cacheFeatures = mutableMapOf<Int, Feature>()

    fun initFeature(){
        if (future != null && !future!!.isDone) {
            return
        }
        isInitSuccess = false
        future = es.submit {
            val features = ArrayList<Feature>()

            // 如果有缓存的话，说明从数据库同步过，从这里设置给sdk的缓存
//            if (cacheFeatures.isNotEmpty()){
//                Log.e("InitFeatureTAG","start0")
//                val time0 = System.currentTimeMillis()
//                features.addAll(cacheFeatures.values)
//                Log.e("InitFeatureTAG"," size = ${features.size}")
//                Log.e("InitFeatureTAG","finish1 -> ${System.currentTimeMillis() - time0}")
////                return@submit
//            }else{  // 如果缓存为空，从数据库查找
//                Log.e("InitFeatureTAG","start1")
//                val time1 = System.currentTimeMillis()
//                val features = ArrayList<Feature>()

//                val userList = mutableListOf<UserEntity>()
//                val count = UserApi.getCount()
//                Log.e("InitFeatureTAG","count = $count size = ${userList.size}")
//                var tmpSize = 0
//                val perSize = count / 4
//                val futures = ArrayList<Future<List<UserEntity>>>()
//                for (i in 0 .. 3){
//                    Log.e("InitFeatureTAG","i = $i")
//                    val end = tmpSize + perSize
//                    if (i != 3){
//                        futures.add(getFuture(tmpSize,end,i))
//                        tmpSize += perSize
//                    }else{
//                        futures.add(getFuture(tmpSize,count,i))
//                    }
//                }
//                val iterator = futures.iterator()
//                while (iterator.hasNext()){
//                    val next = iterator.next()
//                    val get = next.get()
//                    userList.addAll(get)
//                    Log.e("InitFeatureTAG","userList size = ${userList.size}")
//                }
//
//                Log.e("InitFeatureTAG","count = $count size = ${userList.size}")
//
////            // todo 新的方法
//                Log.e("InitFeatureTAG","finish1 -> ${System.currentTimeMillis() - time1}")
//
//                Log.e("InitFeatureTAG","start2")
//                val time2 = System.currentTimeMillis()
//                for (user in userList){
//                    val feature = Feature()
//                    feature.id = user.id.toInt()
////                feature.feature = FileUtils.readFeatureFile(user.featurePath!!)
////                user.feature = feature.feature
//                    feature.feature = user.feature
//                    cacheFeatures[feature.id] = feature
//                    features.add(feature)
//                }
//                Log.e("InitFeatureTAG","finish2 -> ${System.currentTimeMillis() - time2}")
//                WebSocketClient.connect()
            }

//            Log.e("InitFeatureTAG","start3")
//            val time3 = System.currentTimeMillis()
//            synchronized(BdFaceManager.lockObj){
//                BdFaceManager.getFaceFeature().featurePush(features)
//            }
//            Log.e("InitFeatureTAG","finish3 -> ${System.currentTimeMillis() - time3}")


//            val listUser: List<User>? = getAllUserList()
//            for (j in listUser!!.indices) {
//                val feature = Feature()
//                feature.id = listUser[j].id
//                feature.feature = listUser[j].feature
//                features.add(feature)
//            }
//            if (isFeaturePush) {
////                FaceSDKManager.getFaceFeature().featurePush(features)
//                FaceSDKManager.featureList = features
//            }
//            mUserNum = features.size
//            isInitSuccess = true
//            Log.e("FaceSDKManager","人脸缓存特征初始化完成 --> $mUserNum")

//            Log.e("InitFeatureTAG","start4")
//            val time4 = System.currentTimeMillis()
//            val updateRow = UserApi.updateUser(userList)
//            Log.e("InitFeatureTAG","finish4 -> ${System.currentTimeMillis() - time4} ${userList.size} == $updateRow")
        }
    }
}