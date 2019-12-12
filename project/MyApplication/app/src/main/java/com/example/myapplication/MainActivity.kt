package com.example.myapplication

import android.animation.LayoutTransition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import com.example.mylibrary.CarouselView
import com.example.mylibrary.RateView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bForward = findViewById<ImageButton>(R.id.buttonForward)
        val bBackward = findViewById<ImageButton>(R.id.buttonBackward).apply { isEnabled = false }
        val carouselView = findViewById<CarouselView>(R.id.carouselView)
        carouselView.setAdapter(MyCarouselViewAdapter(this))
        carouselView.setOnFirstDisplayItemIndexChangeListener(object: CarouselView.OnFirstDisplayItemIndexChangeListener {
            override fun onFirstDisplayItemIndexChanged(view: CarouselView, index: Int) {
                if (index == 0) {
                    bBackward.isEnabled = false
                    bForward.isEnabled = true
                } else if (index == carouselView.getMaxFristDisplayItemIndex()) {
                    bForward.isEnabled = false
                    bBackward.isEnabled = true
                } else {
                    bBackward.isEnabled = true
                    bForward.isEnabled = true
                }
            }
        })
        bBackward.setOnClickListener { carouselView.setFirstDisplayItemIndex(0) }
        bForward.setOnClickListener { carouselView.setFirstDisplayItemIndex(carouselView.getMaxFristDisplayItemIndex()) }
    }
}
