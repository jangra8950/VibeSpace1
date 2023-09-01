package com.app.vibespace.models.profile

data class ChatRequest(
    var message: String,
    var receiverId: String,
    var senderId: String,
    var sentAt: Long,
    var eventType: String,
    var type:String,
    var connectionsId:String,
    var chatType:String,
    var isRead:Boolean,
    var status:String,
    var isDelivered:Boolean,
)
