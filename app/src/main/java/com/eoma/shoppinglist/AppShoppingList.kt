package com.eoma.shoppinglist

import android.app.Activity
import android.app.Application
import com.eoma.shoppinglist.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class AppShoppingList: Application(), HasActivityInjector {

    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector().init(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector
}