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
class DaoListTest {

    private val db: AppDB = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, AppDB::class.java)
            .allowMainThreadQueries()
            .build()
    private val daoList = db.daoList()
    private val daoListNames = db.daoListNames()

    private val expected = listOf(EntityLists(0,"list0", "Water"),
            EntityLists(1,"list0", "Bread"))
    private val diffList = listOf(EntityLists(2, "list1","Water"),
            EntityLists(3, "list1","Bread"))
    private val listnames = EntityListNames(0, "list0")
    private val listnames1 = EntityListNames(1, "list1")


    @Before
    fun setUp(){
        daoListNames.insert(listnames)
        daoListNames.insert(listnames1)
        daoList.writeNewList(expected)
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun search(){

        daoList.searchlist("list0").test().assertValue {
            expected == it
        }
    }

    @Test
    fun writeDiffList(){
        daoList.writeNewList(diffList)
        daoList.searchlist("list1")
                .test()
                .assertValue {
                    System.out.println(it.toString())
                    diffList == it
                }
    }

    @Test
    fun selectLastUID(){
        //Another way to do testing
        // val testObserver = TestObserver<POJOuid>()
        //daoList.lastuid().subscribe(testObserver)
        //testObserver.values()[0]
        daoList.lastuid().test().assertValue {
            System.out.println(it.uid.toString())
            1 == it.uid
        }
    }

    @Test
    fun updateCrossProperty(){
        expected[0].crossed = true
        daoList.updateCrossProperty(expected[0])
        daoList.searchlist("list0").test().assertValue {
            it[0].crossed
        }
    }
}