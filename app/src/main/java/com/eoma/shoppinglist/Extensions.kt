package com.eoma.shoppinglist

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun Completable.subsAndObsOnBg(): Completable =
        this.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())


fun <T> Maybe<T>.subscribeAndObserveOnBg(): Maybe<T> =
        this.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())


