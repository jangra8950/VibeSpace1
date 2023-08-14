package com.app.vibespace.models.setting

data class UnblockUserModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val block: Block,
        val message: String
    ){
        data class Block(
            val __v: Int,
            val _id: String,
            val creationDate: String,
            val insertDate: Int,
            val otherUserId: String,
            val userId: String
        )
    }
}