package com.cotyoragames.shoppinglist.ui.user.friends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.other.FriendsListAdapter
import com.cotyoragames.shoppinglist.ui.shoppingitemlist.ShoppingItemViewModel
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListActivity
import kotlinx.android.synthetic.main.activity_friends.*

class FriendsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        val itemViewModel: FriendsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)

        val adapter = FriendsListAdapter(listOf(),this)

        itemViewModel.friends.observe(this, Observer{
            adapter.items=it
            adapter.notifyDataSetChanged()
        })

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
}