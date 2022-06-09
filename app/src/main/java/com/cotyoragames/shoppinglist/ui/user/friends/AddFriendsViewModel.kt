package com.cotyoragames.shoppinglist.ui.user.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cotyoragames.shoppinglist.data.db.entities.FriendRequest
import com.cotyoragames.shoppinglist.data.db.entities.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddFriendsViewModel : ViewModel() {
    val db = Firebase.firestore
    val auth = Firebase.auth
    private val _users = MutableLiveData<List<Users>>()
    val users : LiveData<List<Users>>
        get() = _users

    private val _requests = MutableLiveData<List<FriendRequest>>()
    val requests : LiveData<List<FriendRequest>>
        get() = _requests
    init {
        getFriends()
    }
    private fun getFriends(){
        val userList:MutableList<Users> = mutableListOf()
        db.collection("users")
        .get().addOnSuccessListener { docs->
                for (document in docs){
                    val newUser = Users(document["uid"] as String, document["displayName"] as String  ,document["email"] as String ,
                        document["photoUrl"] as String)
                    userList.add(newUser)
                }
                _users.postValue(userList)
        }.addOnFailureListener { ex->

        }
    }

    private fun getRequests()
    {
        db.collection("friendrequest").whereEqualTo("senderId",auth.currentUser!!.uid).get().addOnSuccessListener { docs->
            
        }
    }
}