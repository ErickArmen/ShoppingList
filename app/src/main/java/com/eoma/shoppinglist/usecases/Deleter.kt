package com.eoma.shoppinglist.usecases

import com.eoma.shoppinglist.sqlite.*
import com.eoma.shoppinglist.subsAndObsOnBg
import io.reactivex.Completable
import javax.inject.Inject


class Deleter @Inject constructor(val daoProducts: DaoProducts, val daoListNames: DaoListNames) {

    fun deleteProduct(product: EntityProducts): Completable =
            Completable.fromAction{daoProducts.delete(product)}.subsAndObsOnBg()

    fun deleteList(entity: EntityListNames): Completable =
            Completable.fromAction {daoListNames.deleteList(entity)}.subsAndObsOnBg()
}