package com.cotyoragames.shoppinglist.other

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Users
import kotlinx.android.synthetic.main.friend_item.view.*

class FriendsListAdapter(var items:List<Users>, var context: Context,var activity:Int) :
    RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {
    inner class FriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item,parent,false)
        return  FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        var view=holder.itemView
        var current=items.get(position)
        Glide.with(context).load(current.photoUrl).into(view.friendsimg)
        view.usernametxt.text=current.displayName
        view.friendemailtxt.text=current.email
        if (activity==1)
        {
            view.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}