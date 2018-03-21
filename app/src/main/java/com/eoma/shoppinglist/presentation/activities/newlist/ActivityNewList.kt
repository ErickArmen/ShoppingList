package com.eoma.shoppinglist.presentation.activities.newlist

import android.app.Dialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.eoma.shoppinglist.R
import com.eoma.shoppinglist.presentation.interfaces.InterfaceProducts
import com.eoma.shoppinglist.presentation.interfaces.InterfaceTouchHelper
import com.eoma.shoppinglist.presentation.recycler.ItemAnimator2
import com.eoma.shoppinglist.presentation.recycler.ItemTouchHelper2
import com.eoma.shoppinglist.presentation.recycler.RecyclerAdapter2
import com.eoma.shoppinglist.sqlite.EntityLists
import com.eoma.shoppinglist.sqlite.EntityProducts
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_new_list.*
import kotlinx.android.synthetic.main.dialog_with_edittext.*
import javax.inject.Inject

private var list2 = ArrayList<EntityLists>()
private var lastUID = 0
class ActivityNewList: AppCompatActivity(), InterfaceProducts, InterfaceTouchHelper {

    @Inject lateinit var vmFactory:     ViewModelProvider.Factory
    private lateinit var viewModel:     ViewModelNewList
    private lateinit var adapterProds:  RecyclerAdapter2<EntityProducts>
    private lateinit var listname:      String
    private lateinit var adapterList:   RecyclerAdapter2<EntityLists>
    private val disposable =            CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_list)

        viewModel = ViewModelProviders.of(this, vmFactory).get(ViewModelNewList::class.java)

        getIntents()
        recycler1.layoutManager = LinearLayoutManager(this)
        recycler2.layoutManager = LinearLayoutManager(this)
        recycler1.addItemDecoration( DividerItemDecoration(this, DividerItemDecoration.VERTICAL) )
        recycler2.addItemDecoration( DividerItemDecoration(this, DividerItemDecoration.VERTICAL) )


        disposable.add(
                viewModel.readAllProducts().subscribe {
                    adapterProds = RecyclerAdapter2(it,this)
                    recycler1.adapter = adapterProds
                    ItemTouchHelper(ItemTouchHelper2(this, 1)).attachToRecyclerView(recycler1)
                }
        )

        disposable.add(
                viewModel.readLastUID().subscribe {
                    lastUID = it.uid + 1
                }
        )

        fab.setOnClickListener {
            clickSaveList()
        }


        fabNewProduct.setOnClickListener {
            clickNewProduct()
        }

        adapterList = RecyclerAdapter2(list2)
        recycler2.adapter = adapterList
        ItemTouchHelper(ItemTouchHelper2(this, 2)).attachToRecyclerView(recycler2)
        recycler2.itemAnimator = ItemAnimator2()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun clickProductList(position: Int) {
        val itemproduct = adapterProds.getItem(position).product
        list2.add(EntityLists(lastUID, listname, itemproduct))
        adapterList.notifyItemInserted(list2.size - 1)
        lastUID++
    }

    override fun swipe(position: Int, list: Int) {

        when(list){
            1 ->{
                if (adapterProds.itemCount != 1) {
                    disposable.add(
                            viewModel.deleteProduct(adapterProds.getItem(position)).subscribe(
                                    { adapterProds.swipeAdapter(position) }
                            )
                    )
                }
            }
            2 -> {adapterList.swipeAdapter(position)}
        }

    }

    private fun clickSaveList(){

        if(list2.isNotEmpty()) {

            disposable.add(
                    viewModel.writeNewList(list2).subscribe(

                            {
                                Toast.makeText(this, R.string.list_done, Toast.LENGTH_LONG).show()
                                finish()
                            },
                            {
                                Toast.makeText(this, R.string.list_error, Toast.LENGTH_LONG).show()
                            }
                    )
            )
        }
    }

    private fun clickNewProduct(){

        val dlgClick = DialogInterface.OnClickListener { dialog, _ ->

            dialog as Dialog
            val uid = adapterProds.getItem(adapterProds.itemCount - 1).uid
            val item = EntityProducts(uid+1, dialog.etDialog.text.toString())
            disposable.add(
                    viewModel.addProduct(listOf(item)).subscribe(

                            {
                                adapterProds.addItem(item)
                                adapterProds.notifyItemInserted(adapterProds.itemCount)
                                Toast.makeText(this, R.string.product_added, Toast.LENGTH_LONG).show()
                            },
                            {
                                Toast.makeText(this, R.string.list_error, Toast.LENGTH_LONG).show()
                            }
                    )
            )
        }

        val dlgAlert = AlertDialog.Builder(this, R.style.CustomizedDialog)
        dlgAlert.setView(R.layout.dialog_with_edittext)
        dlgAlert.setMessage(R.string.name_new_product)
        dlgAlert.setPositiveButton("Create", dlgClick)
        dlgAlert.setNegativeButton("No", null)
        dlgAlert.create()
        dlgAlert.show()
    }

    private fun getIntents(){
        listname = intent.extras.getString("listname")
    }
}