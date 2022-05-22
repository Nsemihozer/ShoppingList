package com.cotyoragames.shoppinglist.ui.shoppinglist

import androidx.lifecycle.ViewModel

import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.data.repositories.ShoppingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListViewModel(private val repository: ShoppingRepository) : ViewModel() {


    fun upsert(item: Shoppings)= CoroutineScope(Dispatchers.Main).launch {
        repository.insertShopping(item)
    }

    fun getAllShoppingItems()= repository.getAllShoppings()

    fun getShoppingCounts(itemID:Int)= repository.getShoppingCounts(itemID)

    fun getLastAddedShopping()= repository.getLastShopping()

    fun delete(item: Shoppings)= CoroutineScope(Dispatchers.Main).launch {
        repository.deleteShoppingItems(item.shoppingsId!!)
        repository.deleteShopping(item)

    }
}