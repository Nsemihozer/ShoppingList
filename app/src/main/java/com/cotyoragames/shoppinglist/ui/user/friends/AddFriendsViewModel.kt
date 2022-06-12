package com.cotyoragames.shoppinglist.ui.user.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
        getFriends()
        getSendedRequests()
        getReceivedRequests()
        _status.postValue(0) //0 clear 1 sending 2 success 3 fail
    }

    fun sendRequest(userId:String) {
        val docData = hashMapOf(
            "senderId" to auth.currentUser!!.uid,
            "receiverId" to userId,
            "status" to 0 // waiting
        )
        db.collection("friendrequest").add(docData).addOnSuccessListener {
            getSendedRequests()
           setStatus(2)
        }.addOnFailureListener {
            setStatus(3)
        }
    }

    fun setStatus(status:Int)
    {
        _status.postValue(status)
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

    private fun getSendedRequests()
    {
        val sendedrequestList:MutableList<FriendRequest> = mutableListOf()
        db.collection("friendrequest").whereEqualTo("senderId",auth.currentUser!!.uid).get().addOnSuccessListener { docs->
            for (document in docs){
                val newRequest = FriendRequest(document["senderId"] as String, document["receiverId"] as String  ,
                    (document["status"] as Long).toInt()
                )
                sendedrequestList.add(newRequest)
            }
            _sendedRequests.postValue(sendedrequestList)
        }
    }
    private fun getReceivedRequests()
    {
        val receivedrequestList:MutableList<FriendRequest> = mutableListOf()
        db.collection("friendrequest").whereEqualTo("receiverId",auth.currentUser!!.uid).get().addOnSuccessListener { docs->
            for (document in docs){
                val newRequest = FriendRequest(document["senderId"] as String, document["receiverId"] as String  ,
                    (document["status"] as Long).toInt()
                )
                receivedrequestList.add(newRequest)
            }
            _receivedRequests.postValue(receivedrequestList)
        }
    }
}