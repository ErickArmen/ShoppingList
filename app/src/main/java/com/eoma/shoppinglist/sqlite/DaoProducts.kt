package com.eoma.shoppinglist.sqlite

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
import io.reactivex.Maybe


@Dao
interface DaoProducts{

    @Insert(onConflict = IGNORE)
    fun insert(list: List<EntityProducts>)

    @Query("SELECT * FROM entityproducts")
    fun getAll(): Maybe<MutableList<EntityProducts>>

    @Delete
    fun delete(product: EntityProducts)

}