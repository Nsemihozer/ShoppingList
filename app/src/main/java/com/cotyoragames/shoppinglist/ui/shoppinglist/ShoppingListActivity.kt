package com.cotyoragames.shoppinglist.ui.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.other.ShoppingItemAdapter
import com.cotyoragames.shoppinglist.other.ShoppingListAdapter
import com.cotyoragames.shoppinglist.ui.shoppingitemlist.*
import kotlinx.android.synthetic.main.activity_shopping_item.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ShoppingListActivity : AppCompatActivity() , KodeinAware {

    override val kodein by kodein()
    private val factoryItem: ShoppingListViewModelFactory by instance()
    private lateinit var currItems:List<Shoppings>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        val itemViewModel: ShoppingListViewModel =
            ViewModelProvider(this, factoryItem).get(ShoppingListViewModel::class.java)


        val adapter = ShoppingListAdapter(listOf(), itemViewModel)
        rvShoppingItems.layoutManager = LinearLayoutManager(this)
        rvShoppingItems.adapter = adapter

        itemViewModel.getAllShoppingItems().observe(this, Observer {
            currItems=it
            adapter.items = currItems
            adapter.notifyDataSetChanged()
        })

        fab.setOnClickListener {
            val intent =Intent(this, ShoppingItemActivity::class.java).apply {
                //putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }

    }
}