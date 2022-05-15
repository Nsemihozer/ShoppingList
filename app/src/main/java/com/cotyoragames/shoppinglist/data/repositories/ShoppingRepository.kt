package com.cotyoragames.shoppinglist.data.repositories

import com.cotyoragames.shoppinglist.data.db.ShoppingDatabase
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem

class ShoppingRepository(
    private val db:ShoppingDatabase
) {
    suspend fun upsert(item:ShoppingItem)=db.getShoppingDao().upsert(item)

    suspend fun delete(item: ShoppingItem)=db.getShoppingDao().delete(item)

    suspend fun deleteAll()=db.getShoppingDao().deleteAll()

    fun getAllShoppingItems()=db.getShoppingDao().getAllShoppingItems()
}