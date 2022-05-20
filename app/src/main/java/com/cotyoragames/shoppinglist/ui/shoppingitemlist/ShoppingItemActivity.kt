package com.cotyoragames.shoppinglist.ui.shoppingitemlist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.other.ShoppingItemAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_shopping_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.kodein
class ShoppingItemActivity : AppCompatActivity() , KodeinAware   {

    override val kodein by kodein()
    private val factoryItem:ShoppingItemViewModelFactory by instance()
    private lateinit var currItems:List<ShoppingItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_item)


        val itemViewModel: ShoppingItemViewModel =
            ViewModelProvider(this, factoryItem).get(ShoppingItemViewModel::class.java)


        val adapter = ShoppingItemAdapter(listOf(), itemViewModel)
        rvShoppingItems.layoutManager = LinearLayoutManager(this)
        rvShoppingItems.adapter = adapter

        itemViewModel.getNotListedShoppingItems().observe(this, Observer {
            currItems=it
            adapter.items = currItems
            adapter.notifyDataSetChanged()
        })

        fab.setOnClickListener {
            AddShoppingItemDialog(this, object : AddDialogListener {
                override fun onAddButtonClicked(item: ShoppingItem) {
                    itemViewModel.upsert(item)
                }
            }).show()
        }
        fablogout.setOnClickListener {
            Firebase.auth.signOut()
        }

        btnsend.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO)
                {
                    itemViewModel.upsertShoppings()
                    val shopping = itemViewModel.getLastShopping()
                    for (item in currItems) {
                        item.shoppingId=shopping.shoppingsId!!
                        itemViewModel.send(item)
                    }

                }
                Toast.makeText(applicationContext,"Başarılı",Toast.LENGTH_SHORT).show()
            }


        }


    }
}