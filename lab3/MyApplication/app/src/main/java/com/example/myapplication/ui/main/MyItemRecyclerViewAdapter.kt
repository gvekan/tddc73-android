package com.example.myapplication.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R


import com.example.myapplication.ui.main.MainFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_item.view.*

/**
 * Adapter for recycler view that displays MainViewModel.RepositoryItems.
 * Notify listener when item is clicked.
 */
class MyItemRecyclerViewAdapter(
    private var mItems: List<MainViewModel.RepositoryItem>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as MainViewModel.RepositoryItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.mNameView.text = item.name
        val nameWithOwner = "${item.name}/${item.owner}"
        holder.mNameWithOwnerView.text = nameWithOwner
        holder.mDescription.text = item.description
        val forksText = "Forks ${item.forks}"
        holder.mForksView.text = forksText
        val starsText = "Stars ${item.stars}"
        holder.mStarsView.text = starsText

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mItems.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mNameWithOwnerView: TextView = mView.name_with_owner
        val mDescription: TextView = mView.description
        val mForksView: TextView = mView.forks
        val mStarsView: TextView = mView.stars

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }


    fun replaceItems(items: List<MainViewModel.RepositoryItem>) {
        mItems = items
        notifyDataSetChanged()
    }
}
