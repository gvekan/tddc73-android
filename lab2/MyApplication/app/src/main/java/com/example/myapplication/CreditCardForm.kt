package com.example.myapplication

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

class CreditCardForm : FrameLayout {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.credit_card_form, this, true)
    }
}

    /*isFront = true

    mCardBackLayout = findViewById(R.id.card_back)
    mCardFrontLayout = findViewById(R.id.card_front)


    mSetRightOut = AnimatorInflater.loadAnimator(this, R.animator.card_flip_right_out) as AnimatorSet
    mSetLeftIn = AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_in) as AnimatorSet
    mSetLeftOut = AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_out) as AnimatorSet
    mSetRightIn = AnimatorInflater.loadAnimator(this, R.animator.card_flip_right_in) as AnimatorSet

    val distance = 8000
    val scale = resources.displayMetrics.density * distance
    mCardFrontLayout?.cameraDistance = scale;
    mCardBackLayout?.cameraDistance = scale;

    /*if (savedInstanceState == null) {
        supportFragmentManager.beginTransaction()
            .add(R.id.card_container, CardFrontFragment())
            .commit()
    }*/

    val button = findViewById<Button>(R.id.button_id)
    button.setOnClickListener {
        if (isFront) {
            mSetRightOut?.setTarget(mCardFrontLayout)
            mSetLeftIn?.setTarget(mCardBackLayout)
            mSetRightOut?.start()
            mSetLeftIn?.start()

        } else {
            mSetLeftOut?.setTarget(mCardBackLayout)
            mSetRightIn?.setTarget(mCardFrontLayout)
            mSetLeftOut?.start()
            mSetRightIn?.start()
        }
        isFront = !isFront
    }*/