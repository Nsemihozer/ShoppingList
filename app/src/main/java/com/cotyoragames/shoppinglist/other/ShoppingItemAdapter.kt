package com.cotyoragames.shoppinglist.other


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.ui.shoppingitemlist.ShoppingItemViewModel
import kotlinx.android.synthetic.main.shopping_item.view.*

class ShoppingItemAdapter(
    var items: List<ShoppingItem>,
    private val itemViewModel: ShoppingItemViewModel
):RecyclerView.Adapter<ShoppingItemAdapter.ShoppingViewHolder>() {

    inner class ShoppingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item,parent,false)
        return ShoppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val current = items[position]

        holder.itemView.tvAmount.text="${current.amount}"
        holder.itemView.tvName.text=current.name
        holder.itemView.tvAmountType.text= current.amountType
        holder.itemView.ivDelete.setOnClickListener{
            itemViewModel.delete(current)
        }

        holder.itemView.ivPlus.setOnClickListener{
            current.amount+=1
            itemViewModel.upsert(current)
        }
        holder.itemView.ivMinus.setOnClickListener{
            if (current.amount>0){
                current.amount-=1
                itemViewModel.upsert(current)
            }

        }
    }

    override fun getItemCount(): Int {
       return items.size
    }
}