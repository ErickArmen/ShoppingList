package com.eoma.shoppinglist.di

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import dagger.Module
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton
import android.arch.persistence.room.migration.Migration
import com.eoma.shoppinglist.sqlite.*


const val db_name = "my_products"
@Module(includes = [(ViewModelModule::class)])
class AppModule {

    private var db: AppDB? = null

    @Singleton @Provides
    fun injectRoom(application: Application): AppDB = getInstanceDB(application)

    @Singleton @Provides
    fun injectDaoProducts(db: AppDB): DaoProducts = db.daoProducts()

    @Singleton @Provides
    fun injectDaoList(db: AppDB): DaoList = db.daoList()

    @Singleton @Provides
    fun injectDaoListNames(db: AppDB): DaoListNames = db.daoListNames()



    private fun getInstanceDB(application: Application): AppDB{
        return db ?: buildDB(application).also { db = it }
    }

    private fun buildDB(application: Application) =
            Room.databaseBuilder(application, AppDB::class.java, db_name)
                    .addCallback(object: RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Completable
                                    .fromAction { getInstanceDB(application).daoProducts().insert(preData)}
                                    .subscribeOn(Schedulers.computation())
                                    .subscribe()
                        }
                    })
                    .build()

    val preData = listOf(EntityProducts(0, "Milk"), EntityProducts(1, "Bread"),
            EntityProducts(2, "Water"), EntityProducts(3, "Ham"),
            EntityProducts(4, "Eggs"),EntityProducts(5, "Bananas"),
            EntityProducts(6, "Avocado"), EntityProducts(7, "Apples"),
            EntityProducts(8, "Sausage"), EntityProducts(9, "Chicken"),
            EntityProducts(10 ,"Meat"))
}