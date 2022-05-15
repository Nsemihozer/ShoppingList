package com.cotyoragames.shoppinglist.ui.shoppinglist

import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem

interface AddDialogListener {
    fun onAddButtonClicked(item:ShoppingItem)
}