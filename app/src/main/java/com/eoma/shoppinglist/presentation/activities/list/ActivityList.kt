package com.eoma.shoppinglist.presentation.activities.list

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.eoma.shoppinglist.R
import com.eoma.shoppinglist.presentation.interfaces.InterfaceTouchHelper
import com.eoma.shoppinglist.presentation.recycler.ItemTouchHelper2
import com.eoma.shoppinglist.presentation.recycler.RecyclerAdapter2
import com.eoma.shoppinglist.sqlite.EntityLists
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_list.*
import javax.inject.Inject


class ActivityList: AppCompatActivity(), InterfaceTouchHelper {

    @Inject lateinit var vmFactory:     ViewModelProvider.Factory
    private lateinit var viewModel:     ViewModelList
    private lateinit var listname:      String
    private lateinit var adapter:       RecyclerAdapter2<EntityLists>
    private val disposable =            CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        viewModel = ViewModelProviders.of(this, vmFactory).get(ViewModelList::class.java)

        getIntents()
        toolbar.title = listname
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setTitleTextAppearance(this, R.style.AnandaStyle)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration( DividerItemDecoration(this, DividerItemDecoration.VERTICAL) )

        disposable.add(
                viewModel.readList(listname).subscribe {
                    adapter = RecyclerAdapter2(it as MutableList<EntityLists>)
                    recyclerView.adapter = adapter
                    ItemTouchHelper(ItemTouchHelper2(this)).attachToRecyclerView(recyclerView)
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun swipe(position: Int, list: Int) {

        val item = adapter.getItem(position)
        item.crossed = !item.crossed

        disposable.add(
            viewModel.modifyCrossState(item).subscribe(
                    {adapter.notifyItemChanged(position)},
                    { Toast.makeText(this, R.string.list_error, Toast.LENGTH_SHORT).show()}
            )
        )
    }

    private fun getIntents(){
        listname = intent.extras.getString("listname")
    }

}