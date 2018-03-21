package com.eoma.shoppinglist.presentation.recycler

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import com.eoma.shoppinglist.presentation.interfaces.InterfaceTouchHelper


class ItemTouchHelper2(private val view: InterfaceTouchHelper, val list: Int = 0): ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        return makeFlag(ACTION_STATE_IDLE, RIGHT) or makeFlag(ACTION_STATE_SWIPE, RIGHT)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        if (viewHolder != null){
            view.swipe(viewHolder.adapterPosition, list)
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }
}