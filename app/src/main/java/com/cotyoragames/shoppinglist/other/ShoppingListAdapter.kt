package com.cotyoragames.shoppinglist.other

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListViewModel
import kotlinx.android.synthetic.main.shopping_item.view.*
import kotlinx.android.synthetic.main.shopping_satirlayout.view.*

class ShoppingListAdapter(
    var items: List<Shoppings>,
    private val itemViewModel: ShoppingListViewModel
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
        holder.itemView.adet.text="${itemViewModel.getShoppingCounts(current.shoppingsId!!)}"

        holder.itemView.ivDelete2.setOnClickListener{
            itemViewModel.delete(current)
        }


    }

    override fun getItemCount(): Int {
        return items.size;
    }
}