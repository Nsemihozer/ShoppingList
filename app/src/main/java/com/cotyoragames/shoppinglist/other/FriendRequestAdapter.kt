package com.cotyoragames.shoppinglist.other

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.FriendRequest
import kotlinx.android.synthetic.main.friend_request_item.view.*

class FriendRequestAdapter(var itemList : List<FriendRequest>,var mContext : Context,var userId:String) : RecyclerView.Adapter<FriendRequestAdapter.FrienRequestViewHolder>() {

    inner class FrienRequestViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrienRequestViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.friend_request_item,parent,false)
        return FrienRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: FrienRequestViewHolder, position: Int) {
        val view= holder.itemView
        val current = itemList[position]
        var displayName=""
        if (userId.equals(current.receiverId))
        {
            displayName=current.senderId
            view.requestundobtn.visibility=View.INVISIBLE
        }
        else
        {
            displayName=current.receiverId
            view.requestacceptbtn.visibility=View.INVISIBLE
            view.requestrejectbtn.visibility=View.INVISIBLE
            view.requestundobtn.visibility=View.VISIBLE
        }
        view.requestDisplaytxt.text = displayName

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}