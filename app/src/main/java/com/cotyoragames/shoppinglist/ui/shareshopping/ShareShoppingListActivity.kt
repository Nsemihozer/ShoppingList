package com.cotyoragames.shoppinglist.ui.shareshopping

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
import com.cotyoragames.shoppinglist.ui.user.friends.FriendsViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_share_shopping_list.*

class ShareShoppingListActivity : AppCompatActivity() {
    private lateinit var itemViewModel: ShareShoppingListViewModel
    private lateinit var shopping:Shoppings
    private lateinit var currItems:List<ShoppingItem>
    val db=Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_shopping_list)

        shopping= intent.getSerializableExtra("shopping") as Shoppings
        val args = intent.getBundleExtra("bundle")
        currItems= args!!.getSerializable("items") as List<ShoppingItem>
        itemViewModel = ViewModelProvider(this,ShareShoppingListViewModelFactory(shopping)).get(ShareShoppingListViewModel::class.java)

        val adapter = ShareListAdapter(listOf(),this,)

        itemViewModel.users.observe(this, Observer {
            adapter.items=it
            adapter.notifyDataSetChanged()
        })

        shareuserrw.layoutManager=LinearLayoutManager(this)
        shareuserrw.adapter=adapter



        val docData = hashMapOf(
            "date" to shopping.date,
            "items" to currItems,
        )
        /*db.collection("shoppinglists").add(docData).addOnSuccessListener {

        }.addOnFailureListener {  e -> Log.w("FireStore", "Error writing document", e)  }*/
    }
}