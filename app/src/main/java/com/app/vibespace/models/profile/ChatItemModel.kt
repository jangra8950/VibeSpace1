package com.app.vibespace.models.profile

data class ChatItemModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val chatList: List<Chat>,
        val chatUser: ChatUser,
        val currentUser: CurrentUser
    ){
        data class CurrentUser(
            val _id: String,
            val conversationId: String,
            val firstName: String,
            val mascotIcon: String,
            val profilePic: String,
            val universityId: String,
            val userId: String
        )
        data class ChatUser(
            val _id: String,
            val conversationId: String,
            val firstName: String,
            val lastName: String,
            val mascotIcon: String,
            val profilePic: String,
            val universityId: String,
            val userId: String
        )
        data class Chat(
            val _id: String="",
            val blockByYou: Boolean=false,
            val conversationId: String="",
            val isDelivered: Boolean=false,
            val isOwnMessage: Boolean=true,
            val isRead: Boolean=false,
            val message: String,
            val receiverId: String,
            val senderId: String,
            val sentAt: Long,
            val eventType: String,
            val messageId:String,
            val status:String
        )
    }
}
