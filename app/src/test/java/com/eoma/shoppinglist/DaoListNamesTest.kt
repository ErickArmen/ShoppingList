package com.eoma.shoppinglist

import android.arch.persistence.room.Room
import com.eoma.shoppinglist.sqlite.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class DaoListNamesTest {

    private val db: AppDB = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, AppDB::class.java)
            .allowMainThreadQueries()
            .build()
    private val daoListNames = db.daoListNames()
    private val daoList = db.daoList()

    private val expected = listOf(EntityListNames(0,"list0"), EntityListNames(1,"list1"))
    private val list0 = listOf(EntityLists(0, "list0", "Eggs"),
            EntityLists(1, "list0", "Bread"))


    @Before
    fun setUp(){
        daoListNames.insert(EntityListNames(0,"list0"))
        daoListNames.insert(EntityListNames(1,"list1"))
        daoList.writeNewList(list0)
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun writeRead(){

        daoListNames.readAllNames().test().assertValue {
            expected == it
        }
    }

    @Test
    fun deleteRead(){

        daoListNames.deleteList(expected[0])
        daoListNames.readAllNames().test().assertValue {
            expected[1] == it[0]
        }
    }

    @Test//To prove that it can't exist 2 tables with the same name
    fun writeSameListName(){
        daoListNames.insert(EntityListNames(2,"list1"))
        daoListNames.readAllNames().test().assertValue {
            2 == it.size
        }
    }

    @Test
    fun removeListAndItems(){
        daoListNames.deleteList(expected[0])
        daoList.lastuid().test().assertComplete()
        daoList.searchlist("list0").test().assertValue {
            System.out.print(it.toString())
            it == listOf<EntityLists>()
        }
    }
}