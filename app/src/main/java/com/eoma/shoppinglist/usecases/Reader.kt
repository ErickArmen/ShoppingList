package com.eoma.shoppinglist.usecases

import com.eoma.shoppinglist.sqlite.*
import com.eoma.shoppinglist.subscribeAndObserveOnBg
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class Reader @Inject constructor(val daoProducts: DaoProducts, val daoList: DaoList, val daoListNames: DaoListNames) {

    fun readAllProducts(): Maybe<MutableList<EntityProducts>> =
            daoProducts.getAll().subscribeAndObserveOnBg()

    fun readListNames(): Maybe<MutableList<EntityListNames>> =
            daoListNames.readAllNames().subscribeAndObserveOnBg()

    fun searchListName(name: String): Maybe<POJOlistname> =
            daoListNames.searchListName(name).subscribeAndObserveOnBg()

    fun readList(list: String): Maybe<List<EntityLists>> =
            daoList.searchlist(list).subscribeAndObserveOnBg()

    fun readLastUID(): Maybe<POJOuid> =
            daoList.lastuid().subscribeAndObserveOnBg()

}