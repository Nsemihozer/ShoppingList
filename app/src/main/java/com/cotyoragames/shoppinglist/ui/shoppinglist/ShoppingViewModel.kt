package com.cotyoragames.shoppinglist.ui.shoppinglist

import androidx.lifecycle.ViewModel
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.repositories.ShoppingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingViewModel (
    private val repository: ShoppingRepository
) : ViewModel() {
    fun upsert(item: ShoppingItem)= CoroutineScope(Dispatchers.Main).launch {
        repository.upsert(item)
    }
    fun delete(item:ShoppingItem)= CoroutineScope(Dispatchers.Main).launch {
        repository.delete(item)
    }
    fun getAllShoppingItems()= repository.getAllShoppingItems()

    fun clear()=CoroutineScope(Dispatchers.Main).launch {
        repository.deleteAll()
    }

    fun send()= CoroutineScope(Dispatchers.Main).launch {
        clear()
    }
}