package com.cotyoragames.shoppinglist.ui.shareshopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.data.repositories.ShoppingRepository
import com.cotyoragames.shoppinglist.ui.shoppingitemlist.ShoppingItemViewModel

class ShareShoppingListViewModelFactory(private val shoppings: Shoppings,private val currItems:List<ShoppingItem>) :ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShareShoppingListViewModel(shoppings,currItems) as T
    }
}

