package com.app.vibespace.models.setting

data class BlockedUserListModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val blockUserList: List<BlockUser>,
        val totalCount: Int
    ){
        data class BlockUser(
            val blockId: String,
            val blockedUserDetails: BlockedUserDetails,
            val otherUserId: String,
            val userId: String
        ){
            data class BlockedUserDetails(
                val avatar: String,
                val email: String,
                val firstName: String,
                val gender: String,
                val location: Location,
                val profilePic: String
            ){
                data class Location(
                    val lat: Double,
                    val lng: Double
                )
            }
        }
    }
}