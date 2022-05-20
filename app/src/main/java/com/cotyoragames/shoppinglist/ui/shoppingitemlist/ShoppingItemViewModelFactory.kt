package com.cotyoragames.shoppinglist.ui.shoppingitemlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cotyoragames.shoppinglist.data.repositories.ShoppingRepository

@Suppress("UNCHECKED_CAST")
class ShoppingItemViewModelFactory(
    private val repository: ShoppingRepository
):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShoppingItemViewModel(repository) as T
    }
}