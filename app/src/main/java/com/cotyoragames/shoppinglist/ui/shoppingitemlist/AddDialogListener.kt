package com.cotyoragames.shoppinglist.ui.shoppingitemlist

import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem

interface AddDialogListener {
    fun onAddButtonClicked(item:ShoppingItem)
}