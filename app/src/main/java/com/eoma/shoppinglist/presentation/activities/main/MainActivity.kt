package com.eoma.shoppinglist.presentation.activities.main

import android.app.Dialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.eoma.shoppinglist.R
import com.eoma.shoppinglist.presentation.activities.list.ActivityList
import com.eoma.shoppinglist.presentation.activities.newlist.ActivityNewList
import com.eoma.shoppinglist.presentation.interfaces.InterfaceProducts
import com.eoma.shoppinglist.presentation.interfaces.InterfaceTouchHelper
import com.eoma.shoppinglist.presentation.recycler.ItemTouchHelper2
import com.eoma.shoppinglist.presentation.recycler.RecyclerAdapter2
import com.eoma.shoppinglist.sqlite.EntityListNames
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_with_edittext.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), InterfaceProducts, InterfaceTouchHelper {


    @Inject
    lateinit var vmFactory:         ViewModelProvider.Factory
    private lateinit var viewModel: ViewModelMain
    private lateinit var listname:  String
    private lateinit var adapter:   RecyclerAdapter2<EntityListNames>
    private val disposable =        CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, vmFactory).get(ViewModelMain::class.java)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration( DividerItemDecoration(this, DividerItemDecoration.VERTICAL) )

        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setTitleTextAppearance(this, R.style.AnandaStyle)

        disposable.add(
                viewModel.getListNames().subscribe{
                    adapter = RecyclerAdapter2(it, this)
                    recyclerView.adapter = adapter
                    ItemTouchHelper(ItemTouchHelper2(this)).attachToRecyclerView(recyclerView)
                }
        )

        fab.setOnClickListener {

            showDialog(R.string.new_name_list,
                    R.layout.dialog_with_edittext,
                    {s->
                        disposable.add(
                                viewModel.searchListName(s).subscribe(
                                        {Toast.makeText(this, R.string.name_exists, Toast.LENGTH_SHORT).show()},//success
                                        {Toast.makeText(this, R.string.list_error, Toast.LENGTH_SHORT).show()},//error
                                        {goCreateNewList(s)}                                                      //complete
                                )
                        )
                    },
                    {},{}
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun clickProductList(position: Int) {
        listname = adapter.getItem(position).listname
        goCheckTheList(listname)
    }

    override fun swipe(position: Int, list: Int) {

        showDialog(R.string.delete_this_list, null,
                {
                    val ln = adapter.getItem(position).listname
                    val uid = adapter.getItem(position).uid

                    disposable.add(
                            viewModel.deleteList(EntityListNames(uid, ln)).subscribe(
                                    {
                                        adapter.swipeAdapter(position)
                                        Toast.makeText(this, R.string.deleted, Toast.LENGTH_LONG).show()
                                    },
                                    {
                                        Toast.makeText(this, R.string.list_error, Toast.LENGTH_LONG).show()
                                    }
                            )
                    )
                },
                {adapter.notifyItemChanged(position)},
                {adapter.notifyItemChanged(position)} )
    }



    private fun goCheckTheList(_listname: String){
        val i = Intent(this, ActivityList::class.java)
        i.putExtra("listname", _listname)
        startActivity(i)
    }

    private fun goCreateNewList(_listname: String){

        val uid = if (adapter.itemCount == 0 ) -1 else adapter.getItem(adapter.itemCount-1).uid
        val item = EntityListNames(uid+1, _listname)

        disposable.add(
                viewModel.writeNewNameForList(item).subscribe (

                        {
                            adapter.addItem(item)
                            adapter.notifyItemInserted(adapter.itemCount)
                            val i = Intent(this, ActivityNewList::class.java)
                            i.putExtra("listname", _listname)
                            startActivity(i)
                        },
                        {
                            Toast.makeText(this, R.string.list_error, Toast.LENGTH_LONG).show()
                        }
                )
        )
    }

    private fun showDialog(msg: Int, view: Int? = null, clickPos: (string: String) -> Unit, clickNeg: () -> Unit,
                           clickDismiss: () -> Unit): String{

        var edtxt = "NewList1"

        val dlgClick = DialogInterface.OnClickListener { dialog, _ ->

            if (view != null){
                dialog as Dialog
                edtxt =  dialog.etDialog.text.toString()
            }

            clickPos(edtxt)
        }

        val clickNo = DialogInterface.OnClickListener { _, _ ->
            clickNeg()
        }

        val dlgAlert = AlertDialog.Builder(this, R.style.CustomizedDialog)
        if (view != null) dlgAlert.setView(view)
        dlgAlert.setMessage(msg)
        dlgAlert.setPositiveButton("Yes", dlgClick)
        dlgAlert.setNegativeButton("No", clickNo)
        dlgAlert.setOnCancelListener { clickDismiss() }
        dlgAlert.create()
        dlgAlert.show()

        return edtxt
    }
}
