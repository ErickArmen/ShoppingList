package com.eoma.shoppinglist.presentation.recycler

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eoma.shoppinglist.R
import com.eoma.shoppinglist.presentation.interfaces.InterfaceProducts
import com.eoma.shoppinglist.sqlite.Entity2
import kotlinx.android.synthetic.main.row_main_rv.view.*


class RecyclerAdapter2<T: Entity2>(private val list: MutableList<T>, private val mListener: InterfaceProducts? = null):
        RecyclerView.Adapter<RecyclerViewHolder2>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder2 {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_main_rv, parent, false)
        return RecyclerViewHolder2(itemView, mListener)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder2, position: Int) {

        holder.name.text = list[position].getProductName()

        if (list[position].getPropertyOne()) {
            holder.name.paintFlags = holder.name.paintFlags xor Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int): T = list[position]

    fun addItem(element: T) = list.add(element)

    fun swipeAdapter(position: Int) {
        list.remove(getItem(position))
        notifyItemRemoved(position)
    }

}

class RecyclerViewHolder2(itemView: View, private val mListener: InterfaceProducts?): RecyclerView.ViewHolder(itemView) {

    val name: TextView = itemView.textView

    init {
        itemView.setOnClickListener { mListener?.clickProductList(adapterPosition) }
    }
}

