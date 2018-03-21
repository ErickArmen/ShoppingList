package com.eoma.shoppinglist.sqlite

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey


@Entity(indices = [(Index(value = arrayOf("listname"), unique = true))])
data class EntityListNames(@PrimaryKey val uid: Int, val listname: String): Entity2 {

    override fun getProductName(): String = listname
    override fun getPropertyOne(): Boolean = false

}