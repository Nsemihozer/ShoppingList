package com.cotyoragames.shoppinglist.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shoppings")
data class Shoppings(
    @ColumnInfo(name = "shop_date")
    var date: String,
)
{
    @PrimaryKey(autoGenerate = true)
    var shoppingsId:Int? = null
}
