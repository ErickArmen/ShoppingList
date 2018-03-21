package com.eoma.shoppinglist.di

import com.eoma.shoppinglist.AppShoppingList


class AppInjector {

    fun init(app: AppShoppingList){
        DaggerAppComponent.builder().application(app).build().inject(app)
    }
}