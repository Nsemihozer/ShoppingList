package com.cotyoragames.shoppinglist.ui.insertshopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cotyoragames.shoppinglist.data.repositories.ShoppingRepository

class InsertShoppingViewModelFactory (private val repository: ShoppingRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InsertShoppingViewModel(repository) as T
    }
}
