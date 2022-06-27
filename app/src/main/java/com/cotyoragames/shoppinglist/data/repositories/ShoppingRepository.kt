package com.cotyoragames.shoppinglist.data.repositories

import com.cotyoragames.shoppinglist.data.db.ShoppingDatabase
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings

class ShoppingRepository(
    private val db:ShoppingDatabase
) {

    //-----------------Shopping Item Actions-------------//
    suspend fun upsert(item:ShoppingItem)=db.getShoppingDao().upsert(item)

    suspend fun delete(item: ShoppingItem)=db.getShoppingDao().delete(item)

    suspend fun deleteAll()=db.getShoppingDao().deleteAll()

    suspend fun deleteShoppingItems(shoppingId:Int)=db.getShoppingDao().deleteShoppingItems(shoppingId)

    fun getAllShoppingItems()=db.getShoppingDao().getAllShoppingItems()

    fun getShoppingItems(shoppingId: Int)=db.getShoppingDao().getShoppingItems(shoppingId)

    fun getNotListedShoppingItems()=db.getShoppingDao().getNotListedShoppingItems()

    //----------------------------------------------------//
    //-----------------Shopping  Actions-------------//

    suspend fun deleteShopping(item: Shoppings)=db.getShoppingDao().deleteShopping(item)

    suspend fun insertShopping(item:Shoppings) : Long = db.getShoppingDao().upsertShopping(item)

    fun getAllShoppings()=db.getShoppingDao().getAllShoppings()

    fun getLastShopping()=db.getShoppingDao().getLastShopping()

    fun getShopping(shoppingId: Int) = db.getShoppingDao().getShopping(shoppingId)

    fun getShoppingCounts(itemID:Int)=db.getShoppingDao().getShoppingCounts(itemID)
    //----------------------------------------------------//
}