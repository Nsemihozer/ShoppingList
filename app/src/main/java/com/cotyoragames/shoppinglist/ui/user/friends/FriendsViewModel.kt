package com.cotyoragames.shoppinglist.ui.user.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cotyoragames.shoppinglist.data.db.entities.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendsViewModel : ViewModel() {
    val db = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth
    private val _friends = MutableLiveData<List<Users>>()
    val users : LiveData<List<Users>>
        get() = _friends

    init {
        getFriends()
    }
    private fun getFriends(){
        val query = db.collection("users").whereEqualTo("uid",auth.currentUser!!.uid)

        query.get().addOnSuccessListener { docs->
            val doc = docs.documents[0]
             _friends.postValue(doc["friends"] as List<Users>)
        }
            .addOnFailureListener { ex->

            }
    }
}