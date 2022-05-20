package com.cotyoragames.shoppinglist.data.repositories

import com.cotyoragames.shoppinglist.data.db.ShoppingDatabase
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings

class ShoppingRepository(
    private val db:ShoppingDatabase
) {
    suspend fun upsert(item:ShoppingItem)=db.getShoppingDao().upsert(item)

    suspend fun delete(item: ShoppingItem)=db.getShoppingDao().delete(item)

    suspend fun deleteShopping(item: Shoppings)=db.getShoppingDao().deleteShopping(item)

    suspend fun deleteAll()=db.getShoppingDao().deleteAll()

    fun getAllShoppingItems()=db.getShoppingDao().getAllShoppingItems()

    fun getNotListedShoppingItems()=db.getShoppingDao().getNotListedShoppingItems()

    suspend fun insertShopping(item:Shoppings)=db.getShoppingDao().upsertShopping(item)

    fun getAllShoppings()=db.getShoppingDao().getAllShoppings()

    fun getLastShopping()=db.getShoppingDao().getLastShopping()

    fun getShoppingCounts(itemID:Int)=db.getShoppingDao().getShoppingCounts(itemID)
}