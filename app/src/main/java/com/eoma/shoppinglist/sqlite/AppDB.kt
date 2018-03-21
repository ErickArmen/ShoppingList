package com.eoma.shoppinglist.sqlite

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(entities = [(EntityProducts::class), (EntityLists::class), (EntityListNames::class)],
        version = 1, exportSchema = false)
abstract class AppDB: RoomDatabase() {
    abstract fun daoProducts(): DaoProducts
    abstract fun daoList(): DaoList
    abstract fun daoListNames(): DaoListNames
}