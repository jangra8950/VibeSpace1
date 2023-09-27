package com.app.vibespace.models.profile

data class SummaryModel(
    val apiId: String,
    val data: Data,
    val message: String,
    val statusCode: Int
){
    data class Data(
        val chatSummaryList: List<ChatSummary>
    ){
        data class ChatSummary(
            val blockByYou: Boolean,
            val conversationId: String,
            val deliveredAt: Any,
            val isBlocked: Boolean,
            val isDelivered: Boolean,
            val isOwnMessage: Boolean,
            val isRead: Boolean,
            val message: String,
            val readAt: Any,
            val receiverAvatar: String,
            val receiverFirstName: String,
            val receiverId: String,
            val receiverLastName: String,
            val receiverMascotIcon: String,
            val receiverProfilePic: String,
            val senderAvatar: String,
            val senderFirstName: String,
            val senderId: String,
            val senderLastName: String,
            val senderMascotIcon: String,
            val senderProfilePic: String,
            val sentAt: Long,
            val type: String,
            val unReadCount: Int,
            val spacing:Int
        )
    }
}