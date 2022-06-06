package com.cotyoragames.shoppinglist.ui.user.friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Users
import com.cotyoragames.shoppinglist.other.AutoCompleteFriendAdapter
import kotlinx.android.synthetic.main.activity_add_friends.*

class AddFriendsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        val viewmodel:AddFriendsViewModel= ViewModelProvider(this).get(AddFriendsViewModel::class.java)


        val userAdapter =
            AutoCompleteFriendAdapter(this, R.layout.friend_item, listOf())


        viewmodel.users.observe(this, Observer {
            for (user in it)
                userAdapter.add(user)
        })

        usernameac.setAdapter(userAdapter)
        usernameac.setOnItemClickListener { adapterView, view, i, l ->
            val user = userAdapter.getItem(i) as Users?
            usernameac.setText(user?.displayName)
        }

    }
}