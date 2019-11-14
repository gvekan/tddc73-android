package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {
    companion object {
        var scale = 0f
        var isFront = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val distance = 8000
        scale = resources.displayMetrics.density * distance

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.card_container, CardFrontFragment())
                .commit()
        }

        val button = findViewById<Button>(R.id.button_id)
        button.setOnClickListener {
            isFront = !isFront
            var cardFragment = if (isFront) CardFrontFragment() else CardBackFragment()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                    R.animator.card_flip_left_in, R.animator.card_flip_left_out
                ).replace(R.id.card_container, cardFragment)
                .commit()
        }
    }

    /**
     * A fragment representing the front of the card.
     */
    class CardFrontFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_card_front, container, false)
            view.cameraDistance = scale
            return view
        }
    }

    /**
     * A fragment representing the back of the card.
     */
    class CardBackFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_card_back, container, false)
            view.cameraDistance = scale
            return view
        }
    }



}
