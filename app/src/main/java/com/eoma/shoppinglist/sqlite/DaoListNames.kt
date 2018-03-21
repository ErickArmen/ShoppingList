package com.eoma.shoppinglist.sqlite

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
import io.reactivex.Maybe


@Dao
interface DaoListNames {

    @Insert(onConflict = IGNORE)
    fun insert(listName: EntityListNames)

    @Query("SELECT * FROM entitylistnames")
    fun readAllNames(): Maybe<MutableList<EntityListNames>>

    @Query("SELECT listname FROM entitylistnames WHERE listname = :listname")
    fun searchListName(listname: String): Maybe<POJOlistname>

    @Delete
    fun deleteList(list: EntityListNames)
}