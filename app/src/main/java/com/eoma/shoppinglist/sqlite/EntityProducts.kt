package com.eoma.shoppinglist.sqlite

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class EntityProducts(@PrimaryKey val uid: Int, val product: String): Entity2 {

    override fun getProductName(): String = product
    override fun getPropertyOne(): Boolean = false
}
