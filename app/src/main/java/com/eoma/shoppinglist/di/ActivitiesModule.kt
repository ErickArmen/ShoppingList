package com.eoma.shoppinglist.di

import com.eoma.shoppinglist.presentation.activities.list.ActivityList
import com.eoma.shoppinglist.presentation.activities.main.MainActivity
import com.eoma.shoppinglist.presentation.activities.newlist.ActivityNewList
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivitiesModule {

    @ContributesAndroidInjector
    fun contributeMain(): MainActivity

    @ContributesAndroidInjector
    fun contributeList(): ActivityList

    @ContributesAndroidInjector
    fun contributeNewList(): ActivityNewList

}