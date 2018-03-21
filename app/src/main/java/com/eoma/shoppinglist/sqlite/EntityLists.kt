package com.eoma.shoppinglist.sqlite

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = [(ForeignKey(entity = EntityListNames::class,
        parentColumns = arrayOf("listname"), childColumns = arrayOf("listname"), onDelete = CASCADE))],
        indices = [(Index(value = arrayOf("listname")))])

data class EntityLists(@PrimaryKey val uid: Int, val listname: String, val product: String, var crossed: Boolean = false) :
        Entity2 {

    override fun getProductName(): String = product
    override fun getPropertyOne(): Boolean = crossed

}