package com.neoqee.facemachine.db.api

import com.neoqee.facemachine.db.DbManager
import com.neoqee.facemachine.db.entity.UserEntity

object UserApi {

    private fun userDao() = DbManager.getFaceDatabase()?.userDao()

    fun getCount(): Int {
        return userDao()?.queryUserCount() ?: 0
    }

    fun addUser(userEntity: UserEntity): Long{
        return userDao()?.insertUser(userEntity) ?: -1L
    }

    fun getUserById(id: Long): UserEntity?{
        return userDao()?.queryUserById(id)
    }

}