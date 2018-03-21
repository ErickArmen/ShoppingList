package com.eoma.shoppinglist.presentation.activities.list

import android.arch.lifecycle.ViewModel
import com.eoma.shoppinglist.sqlite.EntityLists
import com.eoma.shoppinglist.usecases.Reader
import com.eoma.shoppinglist.usecases.Writer
import io.reactivex.Maybe
import javax.inject.Inject


class ViewModelList @Inject constructor(val reader: Reader, val writer: Writer): ViewModel() {

    fun readList(list: String): Maybe<List<EntityLists>> = reader.readList(list)

    fun modifyCrossState(cross: EntityLists) = writer.modifyCrossState(cross)

}