package com.example.mylibrary

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

/**
 * An OnTouchListener that registers swipes in four different directions.
 */
abstract class OnSwipeTouchListener(ctx: Context?) : View.OnTouchListener {
    private var mGestureDetector: GestureDetector? = null

    init {
        mGestureDetector = GestureDetector(ctx, object: SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                var result = false
                try {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && abs(
                                velocityX
                            ) > SWIPE_VELOCITY_THRESHOLD
                        ) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                            result = true
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && abs(
                            velocityY
                        ) > SWIPE_VELOCITY_THRESHOLD
                    ) {
                        if (diffY > 0) {
                            onSwipeBottom()
                        } else {
                            onSwipeTop()
                        }
                        result = true
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                return result
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return mGestureDetector!!.onTouchEvent(event)
    }

    open fun onSwipeRight() {}

    open fun onSwipeLeft() {}

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}
}