package com.cotyoragames.shoppinglist.other

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Users
import com.cotyoragames.shoppinglist.ui.shareshopping.ShareShoppingListViewModel
import kotlinx.android.synthetic.main.friend_item.view.*

class ShareListAdapter(var items:List<Users>, var context: Context, var viewmodel : ShareShoppingListViewModel? = null) :
    RecyclerView.Adapter<ShareListAdapter.ShareListViewHolder>()  {

    inner class ShareListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item,parent,false)
        return  ShareListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShareListViewHolder, position: Int) {
        val view=holder.itemView
        val current=items.get(position)
        Glide.with(context).load(current.photoUrl).into(view.friendsimg)
        view.usernametxt.text=current.displayName
        view.friendemailtxt.text=current.email
        view.setOnClickListener {
            viewmodel!!.shareShopping(current)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}