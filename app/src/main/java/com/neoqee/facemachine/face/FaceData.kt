package com.neoqee.facemachine.face

data class FaceAsyncList(
    val insert: List<FaceAsyncData>?,
    val update: List<FaceAsyncData>?,
    val remove: List<FaceAsyncData>?
)

data class FaceAsyncData(
    val id:String,
    val name: String,
    val picture: String? = null // base64
){
    fun mapFailData() = FaceAsyncData(id, name)
}