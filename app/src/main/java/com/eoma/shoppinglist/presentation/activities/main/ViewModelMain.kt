package com.eoma.shoppinglist.presentation.activities.main

import android.arch.lifecycle.ViewModel
import com.eoma.shoppinglist.sqlite.EntityListNames
import com.eoma.shoppinglist.sqlite.POJOlistname
import com.eoma.shoppinglist.usecases.Deleter
import com.eoma.shoppinglist.usecases.Reader
import com.eoma.shoppinglist.usecases.Writer
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject


class ViewModelMain @Inject constructor(val reader: Reader, val deleter: Deleter, val writer: Writer): ViewModel() {

    fun getListNames(): Maybe<MutableList<EntityListNames>> = reader.readListNames()

    fun deleteList(entity: EntityListNames): Completable = deleter.deleteList(entity)

    fun writeNewNameForList(name: EntityListNames): Completable = writer.writeNewNameForList(name)

    fun searchListName(name: String): Maybe<POJOlistname> = reader.searchListName(name)

}