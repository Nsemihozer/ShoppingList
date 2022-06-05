package com.cotyoragames.shoppinglist.other

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Users
import kotlinx.android.synthetic.main.friend_item.view.*
import java.util.*

class AutoCompleteFriendAdapter(
    private val mContext: Context,
    private val resourceId:Int , userList: List<Users>): ArrayAdapter<Users>(mContext,resourceId , userList){
    private val user: MutableList<Users> = ArrayList(userList)
    private var allUsers: List<Users> = ArrayList(userList)


    override fun getCount(): Int {
        return user.size
    }
    override fun getItem(position: Int): Users {
        return user[position]
    }
    override fun getItemId(position: Int): Long {
        return user[position].useruid.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = (context as Activity).layoutInflater
            convertView = inflater.inflate(R.layout.friend_item,parent,false)
        }
        try {
            val user: Users = getItem(position)
            val usernametxt = convertView!!.usernametxt
            usernametxt.text = user.displayName
            val useremailtxt = convertView!!.friendemailtxt
            useremailtxt.text = user.email
            val userphoto = convertView!!.friendsimg
            Glide.with(mContext).load(user.photoUrl).into(userphoto)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertView!!
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any) :String {
                return (resultValue as Users).displayName
            }
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null && constraint.length>=3) {
                    val citySuggestion: MutableList<Users> = ArrayList()
                    for (city in allUsers) {
                        if (city.displayName.lowercase(Locale.getDefault()).startsWith(constraint.toString()
                                .lowercase(Locale.getDefault()))
                        ) {
                            citySuggestion.add(city)
                        }
                    }
                    filterResults.values = citySuggestion
                    filterResults.count = citySuggestion.size
                }
                return filterResults
            }
            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults
            ) {
                user.clear()
                if (results.count > 0) {
                    for (result in results.values as List<*>) {
                        if (result is Users) {
                            user.add(result)
                        }
                    }
                    notifyDataSetChanged()
                } else if (constraint == null) {
                    user.addAll(allUsers)
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}

