package com.cotyoragames.shoppinglist

import android.app.Application
import com.cotyoragames.shoppinglist.data.db.ShoppingDatabase
import com.cotyoragames.shoppinglist.data.repositories.ShoppingRepository
import com.cotyoragames.shoppinglist.ui.shoppingitemlist.ShoppingItemViewModelFactory
import com.cotyoragames.shoppinglist.ui.shoppinglist.ShoppingListViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ShoppingApplication : Application(), KodeinAware{
    override val kodein: Kodein
        get() = Kodein.lazy {
            import(androidXModule(this@ShoppingApplication))
            bind() from singleton {
                ShoppingDatabase(instance())
            }
            bind() from singleton {
                ShoppingRepository(instance())
            }
            bind() from provider {
                ShoppingItemViewModelFactory(instance())
            }
            bind() from provider {
                ShoppingListViewModelFactory(instance())
            }
        }
}