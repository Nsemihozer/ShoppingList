package com.cotyoragames.shoppinglist.ui.user.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddFriendsViewModelFactory(private val friends : List<String>) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddFriendsViewModel(friends) as T
    }
}