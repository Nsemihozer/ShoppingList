package com.cotyoragames.shoppinglist.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {
    @get:Rule
    var instantTaskExecutorRole = InstantTaskExecutorRule()

    private lateinit var database: ShoppingDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup()
    {
        database= Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),ShoppingDatabase::class.java).allowMainThreadQueries().build()
        dao=database.getShoppingDao()
    }

    @After
    fun tearDown()
    {
        database.close()
    }


    @Test
    fun upsertShoppingItem()=runBlockingTest{
        val item = ShoppingItem("name",1.0,"KG")
        dao.upsert(item)

        val allshoppingitems = dao.getAllShoppingItems().getOrAwaitValue()

        assertThat(allshoppingitems).contains(item)
    }

    @Test
    fun deleteShoppingItem()=runBlockingTest{
        val item = ShoppingItem("name",1.0,"KG")

        item.shoppingItemId=dao.upsert(item).toInt()
        dao.delete(item)

        val allshoppingitems = dao.getAllShoppingItems().getOrAwaitValue()
        assertThat(allshoppingitems).doesNotContain(item)
    }
}