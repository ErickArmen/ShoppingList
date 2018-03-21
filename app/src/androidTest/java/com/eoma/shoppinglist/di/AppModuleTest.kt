package com.eoma.shoppinglist.di

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.eoma.shoppinglist.sqlite.AppDB
import com.eoma.shoppinglist.sqlite.EntityProducts
import com.eoma.shoppinglist.sqlite.DaoProducts
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AppModuleTest {

    private var db: AppDB? = null
    private lateinit var dao: DaoProducts
    val preData = listOf(EntityProducts(0, "Milk"), EntityProducts(1, "Bread"),
            EntityProducts(2, "Water"), EntityProducts(3, "Ham"),
            EntityProducts(4, "Eggs"),EntityProducts(5, "Bananas"),
            EntityProducts(6, "Avocado"), EntityProducts(7, "Apples"),
            EntityProducts(8, "Sausage"), EntityProducts(9, "Chicken"),
            EntityProducts(10 ,"Meat"))

    @Before
    fun setUp() {
        db = getInstanceDB()
        dao = db!!.daoProducts()
    }

    @After
    fun closeUp(){
        db!!.close()
    }

    @Test
    fun injectRoom() {
        dao.getAll()
                .test()
                .assertValue {
                    preData == it
                }
    }

    private fun getInstanceDB(): AppDB{
        return db ?: buildDB().also { db = it }
    }

    private fun buildDB() =
            Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDB::class.java)
                    .addCallback(object: RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Completable
                                    .fromAction {getInstanceDB().daoProducts().insert(preData)}
                                    .subscribeOn(Schedulers.computation())
                                    .subscribe()
                        }
                    })
                    .build()

}