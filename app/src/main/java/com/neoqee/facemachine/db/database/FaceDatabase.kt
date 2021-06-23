package com.neoqee.facemachine.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neoqee.facemachine.db.dao.UserDao
import com.neoqee.facemachine.db.entity.UserEntity

@Database(entities = [UserEntity::class],version = 1)
abstract class FaceDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
}