package com.eoma.shoppinglist.sqlite

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import io.reactivex.Maybe

@Dao
interface DaoList {

    @Insert(onConflict = IGNORE)
    fun writeNewList(list: List<EntityLists>)

    @Query("SELECT * FROM entitylists WHERE listname = :listname")
    fun searchlist(listname: String): Maybe<List<EntityLists>>

    @Query("SELECT uid FROM entitylists WHERE uid = (SELECT MAX(uid) FROM entitylists)")
    fun lastuid(): Maybe<POJOuid>

    @Update
    fun updateCrossProperty(item: EntityLists)
}