package com.cotyoragames.shoppinglist.ui.shoppingitemlist

import androidx.lifecycle.ViewModel
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.data.repositories.ShoppingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ShoppingItemViewModel (
    private val repository: ShoppingRepository
) : ViewModel() {

    fun upsert(item: ShoppingItem)= CoroutineScope(Dispatchers.Main).launch {
        repository.upsert(item)
    }
    fun delete(item:ShoppingItem)= CoroutineScope(Dispatchers.Main).launch {
        repository.delete(item)
    }
    fun getShoppingItems(shoppingId:Int)= repository.getShoppingItems(shoppingId)

    fun getNotListedShoppingItems()= repository.getNotListedShoppingItems()

    fun clear()=CoroutineScope(Dispatchers.Main).launch {
        repository.deleteAll()
    }

    fun upsertShoppings()= CoroutineScope(Dispatchers.Main).launch {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        val item = Shoppings(currentDateAndTime)
        repository.insertShopping(item)
    }

    fun send(item:ShoppingItem)= CoroutineScope(Dispatchers.Main).launch {

           upsert(item)
    }
    fun getLastShopping()=repository.getLastShopping()
}