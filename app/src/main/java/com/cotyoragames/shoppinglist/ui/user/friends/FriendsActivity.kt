package com.cotyoragames.shoppinglist.ui.user.friends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.other.FriendsListAdapter
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListActivity
import com.cotyoragames.shoppinglist.ui.user.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_friends.*

class FriendsActivity : AppCompatActivity() {
    private lateinit var itemViewModel: FriendsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        itemViewModel = ViewModelProvider(this).get(FriendsViewModel::class.java)

        val adapter = FriendsListAdapter(listOf(),this)

        itemViewModel.users.observe(this, Observer{
            adapter.items=it
            itemViewModel.setStatus(1)
            adapter.notifyDataSetChanged()
        })

        itemViewModel.status.observe(this, Observer {
            if (it==1){
                friendsrw.visibility= View.VISIBLE
                friendspb.visibility=View.INVISIBLE
            }
            else{
                friendsrw.visibility= View.INVISIBLE
                friendspb.visibility=View.VISIBLE
            }
        })

        friendsrw.layoutManager=LinearLayoutManager(this)
        friendsrw.adapter=adapter

        friendbottomnav.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.list_item_menu -> {
                    startActivity(Intent(applicationContext, ShoppingListActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> true
            }

        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        val item = menu!!.findItem(R.id.action_insert)
        item.isVisible=false
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout_item -> {
            Firebase.auth.signOut()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finishAffinity()
            true
        }
        R.id.action_addfriend->{
            startActivity(Intent(applicationContext, AddFriendsActivity::class.java).apply {
               putExtra("friendlist",itemViewModel.friendList.toTypedArray())
            })
            finishAffinity()
            true
        }
        else -> super.onOptionsItemSelected(item)

    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}