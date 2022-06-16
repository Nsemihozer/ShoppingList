package com.cotyoragames.shoppinglist.other

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.ui.addshopping.AddShoppingActivity
import com.cotyoragames.shoppinglist.ui.shoppingitemlist.ShoppingItemActivity
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListActivity
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListViewModel
import kotlinx.android.synthetic.main.shopping_item.view.*
import kotlinx.android.synthetic.main.shopping_satirlayout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class ShoppingListAdapter(
    var items: List<Shoppings>,
    private val itemViewModel: ShoppingListViewModel,
    var context: Context

): RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    inner class ShoppingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingListAdapter.ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_satirlayout,parent,false)
        return ShoppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListAdapter.ShoppingViewHolder, position: Int) {
        val current = items[position]

        holder.itemView.tarih.text= current.date

        CoroutineScope(Dispatchers.IO).launch {
            var count=0
            count=itemViewModel.getShoppingCounts(current.shoppingsId!!)
            withContext(Dispatchers.Main)
            {
                holder.itemView.adet.text="$count"
            }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ShoppingItemActivity::class.java).apply {
                putExtra("shopping", current)
            }
            context.startActivity(intent)
            (context as ShoppingListActivity).finishAffinity()
        }


        holder.itemView.ivDelete2.setOnClickListener{
            itemViewModel.delete(current)
        }


    }

    override fun getItemCount(): Int {
        return items.size;
    }
}