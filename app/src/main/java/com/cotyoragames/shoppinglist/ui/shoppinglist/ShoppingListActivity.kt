package com.cotyoragames.shoppinglist.ui.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.other.ShoppingListAdapter
import com.cotyoragames.shoppinglist.ui.insertshopping.InsertShoppingActivity
import com.cotyoragames.shoppinglist.ui.shoppingitemlist.ShoppingItemActivity
import com.cotyoragames.shoppinglist.ui.user.LoginActivity
import com.cotyoragames.shoppinglist.ui.user.friends.FriendsActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        MobileAds.initialize(this) {}



        val itemViewModel: ShoppingListViewModel =
            ViewModelProvider(this, factoryItem).get(ShoppingListViewModel::class.java)


        val adapter = ShoppingListAdapter(mutableListOf(), itemViewModel,this)
        shoppinglist.layoutManager = LinearLayoutManager(this)
        shoppinglist.adapter = adapter

        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { ad : NativeAd ->
                adapter.setNativeAd(ad)
                adapter.items = currItems as MutableList<Shoppings?>
                adapter.items.add(0,null)
                adapter.notifyDataSetChanged()
                itemViewModel.setStatus(1)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adapter.items = currItems as MutableList<Shoppings?>
                    adapter.notifyDataSetChanged()
                    itemViewModel.setStatus(1)
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())

        itemViewModel.getAllShoppingItems().observe(this, Observer {
            currItems=it
        })

        itemViewModel.status.observe(this, Observer {
            if (it==1)
            {
                shoppinglist.visibility= View.VISIBLE
                shoppinglistpb.visibility= View.INVISIBLE
            }
            else
            {
                shoppinglist.visibility= View.INVISIBLE
                shoppinglistpb.visibility= View.VISIBLE
            }
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

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val currentDateAndTime: String = simpleDateFormat.format(Date())
            val item = Shoppings(currentDateAndTime)

            CoroutineScope(Dispatchers.IO).launch{
                val shoppingid= itemViewModel.upsert(item)
                item.shoppingsId=shoppingid.toInt()
                val intent =Intent(this@ShoppingListActivity, ShoppingItemActivity::class.java).apply {
                    putExtra("shopping",item)
                }
                startActivity(intent)
                finishAffinity()
            }
        }

    }

    private fun addNullValueInsideArray(data: List<Int>) : List<Int?>
    {
        val newData= arrayListOf<Int?>()
        for (i in data.indices)
        {
            if(i % 4 == 0)
            {
                newData.add(null)
            }
            newData.add(data[i])
        }
        return newData
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        val item = menu!!.findItem(R.id.action_addfriend)
        item.isVisible=false
        return true
    }

    /*override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        Glide.with(this).asBitmap().load(AppCompatResources.getDrawable(this,R.drawable.ic_insert)).into(object : SimpleTarget<Bitmap?>(100,100){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                item.icon=BitmapDrawable(resources,resource)
            }

        })
        return super.onPrepareOptionsMenu(menu)
    }*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout_item -> {
            Firebase.auth.signOut()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finishAffinity()
            true
        }
        R.id.action_insert -> {
            startActivity(Intent(applicationContext, InsertShoppingActivity::class.java))
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