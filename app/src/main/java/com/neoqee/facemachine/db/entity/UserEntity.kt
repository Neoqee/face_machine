package com.neoqee.facemachine.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user",indices = [Index(value = ["user_id"],unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "sex")
    val sex: Int = 1,
    @ColumnInfo(name = "valid_start_time")
    val validStartTime: Long = 0,
    @ColumnInfo(name = "valid_end_time")
    val validEndTime: Long = 0,
    @ColumnInfo(name = "face_pic_path")
    val facePicPath: String?,
    @ColumnInfo(name = "face_feature")
    val faceFeature: ByteArray? = null,
    @ColumnInfo(name = "create_time")
    val createTime: Long,
    @ColumnInfo(name = "update_time")
    val updateTime: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (userName != other.userName) return false
        if (sex != other.sex) return false
        if (validStartTime != other.validStartTime) return false
        if (validEndTime != other.validEndTime) return false
        if (facePicPath != other.facePicPath) return false
        if (faceFeature != null) {
            if (other.faceFeature == null) return false
            if (!faceFeature.contentEquals(other.faceFeature)) return false
        } else if (other.faceFeature != null) return false
        if (createTime != other.createTime) return false
        if (updateTime != other.updateTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + sex
        result = 31 * result + validStartTime.hashCode()
        result = 31 * result + validEndTime.hashCode()
        result = 31 * result + (facePicPath?.hashCode() ?: 0)
        result = 31 * result + (faceFeature?.contentHashCode() ?: 0)
        result = 31 * result + createTime.hashCode()
        result = 31 * result + updateTime.hashCode()
        return result
    }
}