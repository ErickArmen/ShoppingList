package com.eoma.shoppinglist.presentation.activities.newlist

import android.arch.lifecycle.ViewModel
import com.eoma.shoppinglist.sqlite.EntityLists
import com.eoma.shoppinglist.sqlite.EntityProducts
import com.eoma.shoppinglist.sqlite.POJOuid
import com.eoma.shoppinglist.usecases.Deleter
import com.eoma.shoppinglist.usecases.Reader
import com.eoma.shoppinglist.usecases.Writer
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject


class ViewModelNewList @Inject constructor(val reader: Reader, val writer: Writer, val deleter: Deleter): ViewModel() {

    fun readAllProducts(): Maybe<MutableList<EntityProducts>> = reader.readAllProducts()

    fun readLastUID(): Maybe<POJOuid> = reader.readLastUID()

    fun writeNewList(list: List<EntityLists>): Completable = writer.writeNewList(list)

    fun addProduct(product: List<EntityProducts>): Completable = writer.addProduct(product)

    fun deleteProduct(product: EntityProducts): Completable = deleter.deleteProduct(product)
}