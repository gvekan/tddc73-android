package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.mylibrary.CarouselViewAdapter
import com.example.mylibrary.RateView

class MyCarouselViewAdapter(context: Context) : CarouselViewAdapter() {
    private val mContext = context

    private var posters = listOf(
        R.drawable.poster1, R.drawable.poster2, R.drawable.poster3,
        R.drawable.poster4, R.drawable.poster5, R.drawable.poster6,
        R.drawable.poster7, R.drawable.poster8)
    private var ratings = MutableList(getItemCount()) { 0 }

    override fun onCreateView(parent: ViewGroup, position: Int): View {
        var view: View?
        if (position == getItemCount()-1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.my_extra_carousel_item, parent, false)

        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.my_carousel_item, parent, false)
            view.findViewById<ImageView>(R.id.imageView).setImageResource(posters[position])
        }
        view.findViewById<RateView>(R.id.rateView).apply {
            setOnRatingChangeListener(object : RateView.OnRatingChangeListener {
                override fun onRatingChanged(view: RateView, rating: Int) {
                    ratings[position] = rating
                }
            })
            setRating(ratings[position])
        }
        return view
    }

    override fun getItemCount(): Int {
        return posters.count() + 1
    }
}