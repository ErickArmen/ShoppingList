package com.eoma.shoppinglist

import android.arch.persistence.room.Room
import com.eoma.shoppinglist.sqlite.AppDB
import com.eoma.shoppinglist.sqlite.EntityProducts
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class DaoProductsTest {

    private val db: AppDB = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, AppDB::class.java)
            .allowMainThreadQueries()
            .build()
    private val daoProducts = db.daoProducts()

    private val expected = listOf(EntityProducts(0,"Water"),
            EntityProducts(1,"Bread"))

    @Before
    fun setUp(){
        daoProducts.insert(expected)
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun insertgetAll(){

        daoProducts.getAll().test().assertValue {
            expected == it
        }
    }

    @Test
    fun writeDelete(){

        daoProducts.delete(expected[0])
        daoProducts.getAll().test().assertValue {
            expected[1] == it[0]
        }
    }

}