package com.example.mylibrary

import android.view.View
import android.view.ViewGroup

/**
 * An adapter to fill a CarouselView with children.
 */
abstract class CarouselViewAdapter {
    private var mListener: DataSetChangeListener? = null

    abstract fun onCreateView(parent: ViewGroup, position: Int): View

    abstract fun getItemCount(): Int

    fun registerListener(listener: DataSetChangeListener) {
        mListener = listener
    }

    fun unregisterListener() {
        mListener = null
    }

    fun notifyDataSetChanged() {
        mListener?.onDataSetChanged()
    }

    interface DataSetChangeListener {
        fun onDataSetChanged()

        fun onDataSetChanged(position: Int)
    }
}