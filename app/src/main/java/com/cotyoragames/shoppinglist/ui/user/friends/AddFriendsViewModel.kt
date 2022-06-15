package com.cotyoragames.shoppinglist.ui.user.friends

import android.util.Log
import androidx.lifecycle.*
import com.cotyoragames.shoppinglist.data.db.entities.FriendRequest
import com.cotyoragames.shoppinglist.data.db.entities.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class AddFriendsViewModel(val friends:List<String>) : ViewModel() {
    val db = Firebase.firestore
    val auth = Firebase.auth
    private val _users = MutableLiveData<List<Users>>()
    val users : LiveData<List<Users>>
        get() = _users

    private val _sendedrequestList:MutableList<FriendRequest> = mutableListOf()
    private val _receivedrequestList:MutableList<FriendRequest> = mutableListOf()
    private val _sendedRequests = MutableLiveData<List<FriendRequest>>()
    private val _receivedRequests = MutableLiveData<List<FriendRequest>>()
    val requests : LiveData<List<FriendRequest>>
        get() = _sendedRequests.combineWith(_receivedRequests) {sendedRequests,receivedRequests->
            val list: MutableList<FriendRequest> = ArrayList()
            if (sendedRequests != null) {
                list.addAll(sendedRequests)
            }
            if (receivedRequests != null) {
                list.addAll(receivedRequests)
            }
            return@combineWith list
        }


    fun <T, K, R> LiveData<T>.combineWith(
        liveData: LiveData<K>,
        block: (T?, K?) -> R
    ): LiveData<R> {
        val result = MediatorLiveData<R>()
        result.addSource(this) {
            result.value = block(this.value, liveData.value)
        }
        result.addSource(liveData) {
            result.value = block(this.value, liveData.value)
        }
        return result
    }
    private val _status = MutableLiveData<Int>()
    val status : LiveData<Int>
    get() = _status
    init {
        _status.postValue(0) //0 clear 1 sending 2 success 3 fail
        viewModelScope.launch{
            getSendedRequests()
            getReceivedRequests()
            delay(3000)
            getUsers()
        }


    }
    fun sendRequest(userId:String) {
        val docData = hashMapOf(
            "senderId" to auth.currentUser!!.uid,
            "receiverId" to userId,
            "status" to 0 // waiting //1 accepted // rejected
        )
        db.collection("friendrequest").add(docData).addOnSuccessListener {
            viewModelScope.launch { getSendedRequests() }
            setStatus(2)
        }.addOnFailureListener {
            setStatus(3)
        }
    }

    fun setStatus(status:Int)
    {
        _status.postValue(status)
    }

    private fun getUsers(){
        val userList:MutableList<Users> = mutableListOf()
        db.collection("users")
        .get().addOnSuccessListener { docs->
                for (document in docs){
                    val uid : String=document["uid"] as String
                    if (_sendedrequestList.find { it.receiverId == uid } == null && _receivedrequestList.find { it.senderId == uid} == null
                        && !friends.contains(uid) && !uid.equals(auth.currentUser!!.uid)) {
                        val newUser = Users(document["uid"] as String, document["displayName"] as String  ,document["email"] as String ,
                            document["photoUrl"] as String)
                        userList.add(newUser)
                    }

                }
                _users.postValue(userList)
        }.addOnFailureListener { ex->

        }
    }


    private fun getSendedRequests() = CoroutineScope(Dispatchers.IO).launch{
        _sendedrequestList.clear()
        db.collection("friendrequest").whereEqualTo("senderId",auth.currentUser!!.uid).whereEqualTo("status",0).get().addOnSuccessListener { docs->
            for (document in docs){
                val newRequest = FriendRequest(document.id, document["senderId"] as String, document["receiverId"] as String  ,
                    (document["status"] as Long).toInt()
                )
                _sendedrequestList.add(newRequest)
            }
            _sendedRequests.postValue(_sendedrequestList)
        }
    }
    private fun getReceivedRequests() = CoroutineScope(Dispatchers.IO).launch {
        _receivedrequestList.clear()
        db.collection("friendrequest").whereEqualTo("receiverId", auth.currentUser!!.uid)
            .whereEqualTo("status", 0).get().addOnSuccessListener { docs ->
            for (document in docs) {
                val newRequest = FriendRequest(
                    document.id, document["senderId"] as String, document["receiverId"] as String,
                    (document["status"] as Long).toInt()
                )
                _receivedrequestList.add(newRequest)
            }
            _receivedRequests.postValue(_receivedrequestList)

        }
    }

    fun undoRequest(id:String)
    {
        db.collection("friendrequest").document(id).delete().addOnSuccessListener {
            _sendedrequestList.remove(_sendedrequestList.find { it.id==id })
            _sendedRequests.postValue(_sendedrequestList)
        }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
    }

    fun rejectRequest(id:String)
    {
        val req=_receivedrequestList.find { it.id==id }
        db.collection("friendrequest").document(id).update("status",2).addOnSuccessListener {
            _receivedrequestList.remove(req)
            _receivedRequests.postValue(_receivedrequestList)
        }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
    }
    fun acceptRequest(id:String)
    {
        val req=_receivedrequestList.find { it.id==id }!!
        db.collection("friendrequest").document(id).update("status",2).addOnSuccessListener {
            _receivedrequestList.remove(req)
            _receivedRequests.postValue(_receivedrequestList)
        }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
        db.collection("users").whereEqualTo("uid",req.senderId).get().addOnSuccessListener { docs->
            val doc = docs.documents[0]
            val friends:MutableList<String> = doc["friends"] as MutableList<String>
            friends.add(req.receiverId)
            db.collection("users").document(doc.id).update("friends",friends)
        }
        db.collection("users").whereEqualTo("uid", req.receiverId).get().addOnSuccessListener { docs->
            val doc = docs.documents[0]
            val friends:MutableList<String> = doc["friends"] as MutableList<String>
            friends.add(req.senderId)
            db.collection("users").document(doc.id).update("friends",friends)
        }
    }
}