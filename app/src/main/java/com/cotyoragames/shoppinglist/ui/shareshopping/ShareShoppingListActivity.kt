package com.cotyoragames.shoppinglist.ui.shareshopping

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.other.FriendsListAdapter
import com.cotyoragames.shoppinglist.other.ShareListAdapter
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListActivity
import com.cotyoragames.shoppinglist.ui.user.friends.FriendsViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_share_shopping_list.*

class ShareShoppingListActivity : AppCompatActivity() {
    private lateinit var itemViewModel: ShareShoppingListViewModel
    private lateinit var shopping:Shoppings
    private lateinit var currItems:List<List<ShoppingItem>>
    val db=Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_shopping_list)

        shopping= intent.getSerializableExtra("shopping") as Shoppings
        val args = intent.getBundleExtra("bundle")
        currItems= args!!.getSerializable("items") as List<List<ShoppingItem>>
        itemViewModel = ViewModelProvider(this,ShareShoppingListViewModelFactory(shopping,currItems[0])).get(ShareShoppingListViewModel::class.java)

        val adapter = ShareListAdapter(listOf(),this,itemViewModel)

        itemViewModel.users.observe(this, Observer {
            adapter.items=it
            adapter.notifyDataSetChanged()
        })

        shareuserrw.layoutManager=LinearLayoutManager(this)
        shareuserrw.adapter=adapter

    }

    override fun onBackPressed() {
        val intent = Intent(this, ShoppingListActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}