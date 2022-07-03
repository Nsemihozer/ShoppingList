package com.cotyoragames.shoppinglist.other

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.cotyoragames.shoppinglist.R
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.ui.addshopping.AddShoppingActivity
import com.cotyoragames.shoppinglist.ui.shoppingitemlist.ShoppingItemActivity
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListActivity
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListViewModel
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.android.synthetic.main.nativead.view.*
import kotlinx.android.synthetic.main.shopping_item.view.*
import kotlinx.android.synthetic.main.shopping_satirlayout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class ShoppingListAdapter(
    var items: MutableList<Shoppings?>,
    private val itemViewModel: ShoppingListViewModel,
    var context: Context

): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var nativeAd: NativeAd
    fun setNativeAd(ad:NativeAd)
    {
        nativeAd=ad

    }

    inner class AdsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ShoppingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when(viewType){
            1->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.nativead,parent,false)
                return AdsHolder(view)
            }
            else->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_satirlayout,parent,false)
                return ShoppingViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        if (holder is AdsHolder)
        {
            val view = holder.itemView
            val adView: NativeAdView = view.native_ad_view

            val adHeadline = view.primary
            val adBody= view.secondary
            val adBtnAction = view.cta
            val adAppIcon = view.icon
            val adStars = view.rating_bar
            /*val adPrice = findViewById(R.id.adPrice)

            val adStore = findViewById(R.id.adStore)
            val adAdvertiser = findViewById(R.id.adAdvertiser)*/
            //Assign position of views inside the native ad view

            adView.headlineView = adHeadline
            adView.bodyView = adBody
            adView.callToActionView = adBtnAction
            adView.iconView = adAppIcon
            adView.starRatingView = adStars
            /*adView.priceView = adPrice

            adView.storeView = adStore
            adView.advertiserView = adAdvertiser*/
            //Assign Values to View


            (adView.headlineView as TextView).text = nativeAd.headline
            (adView.bodyView as TextView).text = nativeAd.body
            (adView.callToActionView as Button).text = nativeAd.callToAction
            (adView.iconView as ImageView).setImageDrawable(nativeAd.icon?.drawable)
            //(adView.priceView as TextView).text = price
            //(adView.storeView as TextView).text = store
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            //(adView.advertiserView as TextView).text = advertiser
            adView.setNativeAd(nativeAd)

        }
        else
        {
            val current = items[position]!!
            holder.itemView.tarih.text= current.date

            CoroutineScope(Dispatchers.IO).launch {
                var count=0
                count=itemViewModel.getShoppingCounts(current.shoppingsId!!)
                withContext(Dispatchers.Main)
                {
                    holder.itemView.adet.text="$count"
                }
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ShoppingItemActivity::class.java).apply {
                    putExtra("shopping", current)
                }
                context.startActivity(intent)
                (context as ShoppingListActivity).finishAffinity()
            }


            holder.itemView.ivDelete2.setOnClickListener{
                itemViewModel.delete(current)
            }
        }



    }

    override fun getItemCount(): Int {
        return items.size;
    }

    override fun getItemViewType(position: Int): Int {
        if (position==0 && items[position] == null)
        {
            return 1
        }
        else
        {
            return 2
        }
    }
}