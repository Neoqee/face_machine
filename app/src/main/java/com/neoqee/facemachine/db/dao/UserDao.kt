package com.neoqee.facemachine.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.neoqee.facemachine.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insertUser(userEntity: UserEntity): Long

    @Query("select count(id) from user")
    fun queryUserCount(): Int

    @Query("select * from user")
    fun queryAllUsers(): Flow<List<UserEntity>>

    @Query("select * from user limit :size offset :offset")
    fun queryUsers(offset: Int, size: Int): Flow<List<UserEntity>>

    @Query("select * from user where id = :id")
    fun queryUserById(id: Long): UserEntity?

    @Query("select * from user where user_id = :userId")
    fun queryUserByUserId(userId: String): Flow<UserEntity?>

    @Update
    fun updateUser(userEntity: UserEntity): Int

    @Update
    fun updateUsers(userEntitys: List<UserEntity>): Int

    @Query("delete from user where id = :id")
    fun deleteUserById(id: Long): Int

    @Query("delete from user where user_id = :userId")
    fun deleteUserByUserId(userId: String): Int
}