package com.cotyoragames.shoppinglist.ui.shoppingitemlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.other.ShoppingItemAdapter
import com.cotyoragames.shoppinglist.ui.shareshopping.ShareShoppingListActivity
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_shopping_item.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.Serializable

class ShoppingItemActivity : AppCompatActivity() , KodeinAware   {

    override val kodein by kodein()
    private val factoryItem:ShoppingItemViewModelFactory by instance()
    private lateinit var currItems:List<ShoppingItem>
    //private var shoppingId:Int = 0
    private lateinit var shoppings:Shoppings
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_item)

        shoppings= intent.getSerializableExtra("shopping") as Shoppings

        val itemViewModel: ShoppingItemViewModel =
            ViewModelProvider(this, factoryItem).get(ShoppingItemViewModel::class.java)


        val adapter = ShoppingItemAdapter(listOf(), itemViewModel)
        rvShoppingItems.layoutManager = LinearLayoutManager(this)
        rvShoppingItems.adapter = adapter

        itemViewModel.getShoppingItems(shoppings.shoppingsId!!).observe(this, Observer {
            currItems=it
            adapter.items = currItems
            adapter.notifyDataSetChanged()
        })

        fab.setOnClickListener {
            AddShoppingItemDialog(this, object : AddDialogListener {
                override fun onAddButtonClicked(item: ShoppingItem) {
                    item.shoppingId=shoppings.shoppingsId!!
                    itemViewModel.upsert(item)
                }
            }).show()
        }


        btnsend.setOnClickListener {
            if (currItems.isNotEmpty())
            {
                val intent = Intent(this@ShoppingItemActivity, ShareShoppingListActivity::class.java).apply {
                    putExtra("shopping",shoppings)
                    val args = Bundle()
                    args.putSerializable("items", arrayListOf(currItems))
                    putExtra("bundle", args)
                }
                startActivity(intent)
                finishAffinity()
            }
           else
            {
                Toast.makeText(this, "At least 1 item", Toast.LENGTH_SHORT).show()
            }

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ShoppingItemActivity, ShoppingListActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}