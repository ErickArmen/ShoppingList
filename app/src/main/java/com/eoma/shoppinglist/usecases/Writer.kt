package com.eoma.shoppinglist.usecases


import com.eoma.shoppinglist.sqlite.*
import com.eoma.shoppinglist.subsAndObsOnBg
import io.reactivex.*
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class Writer @Inject constructor(val daoList: DaoList, val daoProducts: DaoProducts, val daoListNames: DaoListNames) {

    fun addProduct(product: List<EntityProducts>): Completable =
            Completable.fromAction { daoProducts.insert(product) }.subsAndObsOnBg()

    fun writeNewNameForList(name: EntityListNames): Completable =
            Completable.fromAction{ daoListNames.insert(name) }.subsAndObsOnBg()

    fun writeNewList(list: List<EntityLists>): Completable =
            Completable.fromAction { daoList.writeNewList(list) }.subsAndObsOnBg()

    fun modifyCrossState(cross: EntityLists): Completable =
            Completable.fromAction { daoList.updateCrossProperty(cross) }.subsAndObsOnBg()
}

