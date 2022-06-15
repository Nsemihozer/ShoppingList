package com.cotyoragames.shoppinglist.ui.user.friends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        val friends = intent.getStringArrayExtra("friendlist")
        val viewmodel:AddFriendsViewModel= ViewModelProvider(this,AddFriendsViewModelFactory(friends!!.toList())).get(AddFriendsViewModel::class.java)


        val userAdapter =
            AutoCompleteFriendAdapter(this, R.layout.friend_item, listOf())

        val requestAdapter=FriendRequestAdapter(listOf(),this,Firebase.auth.currentUser!!.uid,viewmodel)

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
                viewmodel.setStatus(1)
                viewmodel.sendRequest(useridtext.text.toString())
            }

        }

        viewmodel.status.observe(this, Observer {
            when (it) {
                2 -> {
                    Toast.makeText(this, "Send request", Toast.LENGTH_SHORT).show()
                    friendrequestbtn.visibility= View.VISIBLE
                    viewmodel.setStatus(0)
                }
                3 -> {
                    Toast.makeText(this, "Failed to send request", Toast.LENGTH_SHORT).show()
                    friendrequestbtn.visibility= View.VISIBLE
                    viewmodel.setStatus(0)
                }
                1 -> {
                    friendrequestbtn.visibility= View.GONE
                    friendrequestpb.visibility=View.VISIBLE
                }
                0->{
                    friendrequestpb.visibility=View.GONE
                }
            }
        })

        usernameac.setAdapter(userAdapter)
        usernameac.setOnItemClickListener { adapterView, view, i, l ->
            val user = userAdapter.getItem(i) as Users?
            usernameac.setText(user?.displayName)
            useridtext.text=user?.useruid
        }
        friendrequestrw.layoutManager=LinearLayoutManager(this)
        friendrequestrw.adapter=requestAdapter

    }

    override fun onBackPressed() {
        intent= Intent(this,FriendsActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}