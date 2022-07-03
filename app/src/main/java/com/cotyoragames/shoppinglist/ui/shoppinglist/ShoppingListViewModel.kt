package com.cotyoragames.shoppinglist.ui.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.data.repositories.ShoppingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListViewModel(private val repository: ShoppingRepository) : ViewModel() {

    private val _status = MutableLiveData<Int>()
    val status : LiveData<Int>
        get() = _status

    init {
        _status.postValue(0) //0 loading 1 loaded
    }

    suspend fun upsert(item: Shoppings) = repository.insertShopping(item)

    fun getAllShoppingItems()= repository.getAllShoppings()

    fun getShoppingCounts(itemID:Int)= repository.getShoppingCounts(itemID)

    fun getLastAddedShopping()= repository.getLastShopping()

    fun delete(item: Shoppings)= CoroutineScope(Dispatchers.Main).launch {
        repository.deleteShoppingItems(item.shoppingsId!!)
        repository.deleteShopping(item)
    }
    fun setStatus(status:Int)
    {
        _status.postValue(status)
    }
}