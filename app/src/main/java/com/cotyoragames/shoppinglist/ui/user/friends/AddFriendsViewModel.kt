package com.cotyoragames.shoppinglist.ui.user.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cotyoragames.shoppinglist.data.db.entities.Users
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddFriendsViewModel : ViewModel() {
    val db = Firebase.firestore

    private val _users = MutableLiveData<List<Users>>()
    val users : LiveData<List<Users>>
        get() = _users

    init {
        getFriends()
    }
    private fun getFriends(){
        var userList:MutableList<Users> = mutableListOf()
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
}