package com.cotyoragames.shoppinglist.data.db.entities

data class FriendRequest(
    var id : String,
    var senderId: String,
    var receiverId: String,
    var status:Int
)
