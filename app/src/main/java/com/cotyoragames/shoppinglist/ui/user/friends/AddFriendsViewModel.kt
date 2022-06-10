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

    private val _status = MutableLiveData<Int>()
    val status : LiveData<Int>
    get() = _status
    init {
        getRequests()
        getFriends()
        _status.postValue(0) //0 clear 1 success 2 fail
    }

    fun sendRequest(userId:String) {
        val docData = hashMapOf(
            "senderId" to userId,
            "receiverId" to auth.currentUser!!.uid,
            "status" to 0 // waiting
        )
        db.collection("friendrequest").add(docData).addOnSuccessListener {
            _status.postValue(1)
        }.addOnFailureListener {
            _status.postValue(2)
        }
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
        val requestList:MutableList<FriendRequest> = mutableListOf()
        db.collection("friendrequest").whereEqualTo("senderId",auth.currentUser!!.uid).get().addOnSuccessListener { docs->
            for (document in docs){
                val newRequest = FriendRequest(document["senderId"] as String, document["receiverId"] as String  ,document["status"] as Int)
                requestList.add(newRequest)
            }
            _requests.postValue(requestList)
        }
    }
}