package com.cotyoragames.shoppinglist.ui.shoppingitemlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.other.ShoppingItemAdapter
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
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
    private var shoppingId:Int = 0
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_item)
        shoppingId= intent.getIntExtra("shoppingId",0)

        val itemViewModel: ShoppingItemViewModel =
            ViewModelProvider(this, factoryItem).get(ShoppingItemViewModel::class.java)


        val adapter = ShoppingItemAdapter(listOf(), itemViewModel)
        rvShoppingItems.layoutManager = LinearLayoutManager(this)
        rvShoppingItems.adapter = adapter

        itemViewModel.getShoppingItems(shoppingId).observe(this, Observer {
            currItems=it
            adapter.items = currItems
            adapter.notifyDataSetChanged()
        })

        fab.setOnClickListener {
            AddShoppingItemDialog(this, object : AddDialogListener {
                override fun onAddButtonClicked(item: ShoppingItem) {
                    item.shoppingId=shoppingId
                    itemViewModel.upsert(item)
                }
            }).show()
        }


        btnsend.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO)
                {
                    val shopping= itemViewModel.getShopping(shoppingId)
                    val docData = hashMapOf(
                        "date" to shopping.date,
                        "shoppingId" to shoppingId,
                        "items" to currItems,
                    )
                    db.collection("shoppinglists").add(docData).addOnSuccessListener {

                    }.addOnFailureListener {  e -> Log.w("FireStore", "Error writing document", e)  }
                }
                val intent = Intent(this@ShoppingItemActivity, ShoppingListActivity::class.java)
                startActivity(intent)
                finishAffinity()
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