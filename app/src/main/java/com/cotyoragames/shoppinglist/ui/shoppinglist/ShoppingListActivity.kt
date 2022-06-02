package com.cotyoragames.shoppinglist.ui.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.other.ShoppingListAdapter
import com.cotyoragames.shoppinglist.ui.shoppingitemlist.ShoppingItemActivity
import com.cotyoragames.shoppinglist.ui.user.LoginActivity
import com.cotyoragames.shoppinglist.ui.user.friends.FriendsActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class ShoppingListActivity : AppCompatActivity() , KodeinAware {

    override val kodein by kodein()
    private val factoryItem: ShoppingListViewModelFactory by instance()
    private lateinit var currItems:List<Shoppings>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        val itemViewModel: ShoppingListViewModel =
            ViewModelProvider(this, factoryItem).get(ShoppingListViewModel::class.java)


        val adapter = ShoppingListAdapter(listOf(), itemViewModel,this)
        shoppinglist.layoutManager = LinearLayoutManager(this)
        shoppinglist.adapter = adapter

        itemViewModel.getAllShoppingItems().observe(this, Observer {
            currItems=it
            adapter.items = currItems
            adapter.notifyDataSetChanged()
        })
        bottomNavigationView.selectedItemId=R.id.list_item_menu
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.user_item_menu -> {
                    startActivity(Intent(applicationContext, FriendsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> true
            }
        }

        fab2.setOnClickListener {
            var shoppingId=0
            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val currentDateAndTime: String = simpleDateFormat.format(Date())
            val item = Shoppings(currentDateAndTime)
            itemViewModel.upsert(item)
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO){
                    shoppingId=itemViewModel.getLastAddedShopping().shoppingsId!!
                }
                val intent =Intent(this@ShoppingListActivity, ShoppingItemActivity::class.java).apply {
                    putExtra("shoppingId",shoppingId)
                }
                startActivity(intent)
                finishAffinity()
            }

        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout_item -> {
            Firebase.auth.signOut()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
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