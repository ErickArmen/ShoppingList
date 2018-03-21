package com.eoma.shoppinglist.presentation.recycler


import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.animation.*


class ItemAnimator2: DefaultItemAnimator(){

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {

        val animSet = AnimationSet(true)

        val animAlpha = AlphaAnimation(0f, 1f)
        animAlpha.duration = 1000

        val animTranslate = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f
        )
        animTranslate.duration = 250

        animSet.addAnimation(animAlpha)
        animSet.addAnimation(animTranslate)
        holder?.itemView?.startAnimation(animSet)

        return super.animateAdd(holder)
    }


}