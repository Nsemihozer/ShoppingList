package com.cotyoragames.shoppinglist.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable



@Entity(tableName = "shoppings")
data class Shoppings(
    @ColumnInfo(name = "shop_date")
    var date: String,
) : Serializable
{
    @PrimaryKey(autoGenerate = true)
    var shoppingsId:Int? = null

}
