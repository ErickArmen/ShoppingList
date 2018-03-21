package com.eoma.shoppinglist.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.eoma.shoppinglist.ViewModelFactory
import com.eoma.shoppinglist.presentation.activities.list.ViewModelList
import com.eoma.shoppinglist.presentation.activities.main.ViewModelMain
import com.eoma.shoppinglist.presentation.activities.newlist.ViewModelNewList
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelNewList::class)
    fun bindNewList(myViewModel: ViewModelNewList): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelList::class)
    fun bindList(myViewModel: ViewModelList): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMain::class)
    fun bindMainActivity(myViewModel: ViewModelMain): ViewModel
}