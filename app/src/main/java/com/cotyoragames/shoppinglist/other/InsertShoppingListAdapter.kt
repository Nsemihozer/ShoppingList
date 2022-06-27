package com.cotyoragames.shoppinglist.other

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.SharedShopping
import com.cotyoragames.shoppinglist.ui.insertshopping.InsertShoppingViewModel
import kotlinx.android.synthetic.main.shared_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsertShoppingListAdapter(
    var items:List<SharedShopping>,
    private val mContext: Context,
    private val viewModel: InsertShoppingViewModel ) : RecyclerView.Adapter<InsertShoppingListAdapter.InsertShoppingListViewHolder>() {

    inner class InsertShoppingListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsertShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shared_item,parent,false)
        return  InsertShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: InsertShoppingListViewHolder, position: Int) {
        val view=holder.itemView
        val current=items.get(position)
        view.shareddate.text=current.date
        view.sharedusername.text=current.senderName
    }

    override fun getItemCount(): Int {
      return  items.size
    }

    fun accept(adapterPosition: Int)
    {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.accept(items.get(adapterPosition))
        }

    }


}