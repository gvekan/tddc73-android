package com.example.myapplication

import android.animation.LayoutTransition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import com.example.mylibrary.CarouselView
import com.example.mylibrary.RateView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val carouselView = findViewById<CarouselView>(R.id.carouselView)
        carouselView.setAdapter(MyCarouselViewAdapter(this))
    }
}
