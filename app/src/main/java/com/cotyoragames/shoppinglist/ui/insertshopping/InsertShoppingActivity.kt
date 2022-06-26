package com.cotyoragames.shoppinglist.ui.insertshopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.other.InsertShoppingListAdapter
import com.cotyoragames.shoppinglist.other.SwipeGesture
import kotlinx.android.synthetic.main.activity_insert_shopping.*

class InsertShoppingActivity : AppCompatActivity() {
    private lateinit var itemViewModel: InsertShoppingViewModel
    private lateinit var adapter: InsertShoppingListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_shopping)
        itemViewModel= ViewModelProvider(this).get(InsertShoppingViewModel::class.java)
        adapter= InsertShoppingListAdapter(listOf(),this,itemViewModel)

        val swipeGesture = object :SwipeGesture(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        adapter.accept(viewHolder.adapterPosition)

                    }
                }
                adapter.notifyItemChanged(viewHolder.getAdapterPosition())
            }
        }
        val swipe = ItemTouchHelper(swipeGesture)
        swipe.attachToRecyclerView(sharedlistrw)
        sharedlistrw.layoutManager=LinearLayoutManager(this)
        sharedlistrw.adapter=adapter

        itemViewModel.shoppings.observe(this, Observer {
            adapter.items=it
            adapter.notifyDataSetChanged()
        })


    }
}