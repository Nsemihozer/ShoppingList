package com.cotyoragames.shoppinglist.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable



@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @ColumnInfo(name = "item_name")
    var name: String,
    @ColumnInfo(name="item_amount")
    var amount:Double,
    @ColumnInfo(name="amount_type")
    var amountType:String,
) : Serializable
{
    @PrimaryKey(autoGenerate = true)
    var shoppingItemId:Int? = null
    @ColumnInfo(name="is_bought")
    var isBought:Boolean = false
    @ColumnInfo(name="shoppingId")
    var shoppingId:Int=0
}
