package com.cotyoragames.shoppinglist.ui.user.friends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListActivity
import kotlinx.android.synthetic.main.activity_friends.*

class FriendsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

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