package com.cotyoragames.shoppinglist.ui.user.friends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Users
import com.cotyoragames.shoppinglist.other.AutoCompleteFriendAdapter
import com.cotyoragames.shoppinglist.other.FriendRequestAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_friends.*

class AddFriendsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        val viewmodel:AddFriendsViewModel= ViewModelProvider(this).get(AddFriendsViewModel::class.java)


        val userAdapter =
            AutoCompleteFriendAdapter(this, R.layout.friend_item, listOf())

        val requestAdapter=FriendRequestAdapter(listOf(),this,Firebase.auth.currentUser!!.uid)

        viewmodel.requests.observe(this, Observer {
            requestAdapter.itemList=it
            requestAdapter.notifyDataSetChanged()
        })

        viewmodel.users.observe(this, Observer {
            userAdapter.allUsers=it
            userAdapter.notifyDataSetChanged()
        })

        friendrequestbtn.setOnClickListener{
            if (useridtext.text.toString().isEmpty())
            {
                Toast.makeText(this, "Please First Enter User", Toast.LENGTH_SHORT).show()
            }
            else
            {
                viewmodel.sendRequest(useridtext.text.toString())
            }

        }

        viewmodel.status.observe(this, Observer {
            if (it==1)
            {
                Toast.makeText(this, "Send request", Toast.LENGTH_SHORT).show()
            }
            else if (it ==2)
            {
                Toast.makeText(this, "Failed to send request", Toast.LENGTH_SHORT).show()
            }
        })

        usernameac.setAdapter(userAdapter)
        usernameac.setOnItemClickListener { adapterView, view, i, l ->
            val user = userAdapter.getItem(i) as Users?
            usernameac.setText(user?.displayName)
        }

    }
}