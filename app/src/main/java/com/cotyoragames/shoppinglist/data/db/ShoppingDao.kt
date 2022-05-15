package com.cotyoragames.shoppinglist.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings

@Dao
interface ShoppingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ShoppingItem)
    @Delete
    suspend fun delete(item: ShoppingItem)
    @Query("select * from shopping_items")
    fun getAllShoppingItems(): LiveData<List<ShoppingItem>>
    @Query("DELETE FROM shopping_items")
    suspend fun deleteAll()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertShopping(item: Shoppings)
    @Delete
    suspend fun deleteShopping(item: Shoppings)
    @Query("select * from shoppings")
    fun getAllShoppings(): LiveData<List<Shoppings>>
    @Query("DELETE FROM shoppings")
    suspend fun deleteAllShoppings()
    @Query("select * from shopping_items where shoppingId=:shoppingId")
    fun getShoppingItems(shoppingId: Int): LiveData<List<ShoppingItem>>
}