package com.cotyoragames.shoppinglist.ui.insertshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cotyoragames.shoppinglist.data.db.entities.SharedShopping
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.data.repositories.ShoppingRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InsertShoppingViewModel(private val repository: ShoppingRepository) : ViewModel() {

    private val db=Firebase.firestore
    private val auth=Firebase.auth
    private val _shoppingList  = mutableListOf<SharedShopping>()
    private val _shoppingItemList  = mutableListOf<ShoppingItem>()
    private val _shoppings = MutableLiveData<List<SharedShopping>>()
    val shoppings : LiveData<List<SharedShopping>>
        get()=_shoppings

    init {
        getShoppings()
    }
    fun getShoppings()
    {
        _shoppingList.clear()
        db.collection("shoppinglists").whereEqualTo("receiverId",auth.currentUser!!.uid).get().addOnSuccessListener { query->
            val docs= query.documents
            for (doc in docs)
            {
                val senderName=doc["senderName"] as String
                val date=doc["date"] as String
                val item = SharedShopping (
                    date,
                    senderName,
                    doc.id
                )
                _shoppingList.add(item)
            }
            _shoppings.postValue(_shoppingList)
        }
    }

    suspend fun accept(shareditem:SharedShopping){
        db.collection("shoppinglists").document(shareditem.shareid).get().addOnSuccessListener { doc->
            val items= doc["items"] as List<HashMap<String,Any>>
            CoroutineScope(Dispatchers.IO).launch {
                val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val currentDateAndTime: String = simpleDateFormat.format(Date())
                val item = Shoppings(currentDateAndTime)
                val id= repository.insertShopping(item)
                for (shopitem in items)
                {
                    val newitem = ShoppingItem(shopitem["name"] as String,shopitem["amount"] as Double,shopitem["amountType"] as String)
                    newitem.shoppingId=id.toInt()
                    repository.upsert(newitem)
                }
            }
        }
    }
}