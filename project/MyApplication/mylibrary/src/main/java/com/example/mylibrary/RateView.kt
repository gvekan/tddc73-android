package com.example.mylibrary

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import java.lang.Exception

/**
 * A compound ViewGroup.
 */
class RateView : ConstraintLayout {


    /**
     * Default attributes values.
     */

    val DEFAULT_MAX_RATING = 3
    val DEFAULT_RATING = 0
    val DEFAULT_FILLED_SYMBOL = R.drawable.default_filled_rate_symbol
    val DEFAULT_UNFILLED_SYMBOL = R.drawable.default_unfilled_rate_symbol
    val DEFAULT_EDITABLE = true


    /**
     * Private attributes fields
     */

    private var mMaxRating: Int = DEFAULT_MAX_RATING
    private var mRating: Int = DEFAULT_RATING
    private var mFilledRateSymbol: Int = DEFAULT_FILLED_SYMBOL
    private var mUnfilledRateSymbol: Int = DEFAULT_UNFILLED_SYMBOL


    /**
     * Other private fields.
     */

    private var mListener: OnRatingChangeListener? = null
    private var mRateSymbols: List<ImageView> = listOf()


    /**
     * Public attributes fields.
     */

    var editable: Boolean = DEFAULT_EDITABLE


    /**
     * Constructors.
     */

    constructor(context: Context?) : super(context) {
        init(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init(context, attrs)
    }
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet? = null) {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.RateView,
            0, 0)?.apply {

            try {
                mMaxRating = getInteger(
                    R.styleable.RateView_maxRating,
                    DEFAULT_MAX_RATING
                ).also { validateMaxRating(it) }
                mRating = getInteger(
                    R.styleable.RateView_rating,
                    DEFAULT_RATING
                ).also { validateRating(it) }
                mFilledRateSymbol = getResourceId(
                    R.styleable.RateView_filledSymbol,
                    DEFAULT_FILLED_SYMBOL
                )
                mUnfilledRateSymbol = getResourceId(
                    R.styleable.RateView_unfilledSymbol,
                    DEFAULT_UNFILLED_SYMBOL
                )
                editable = getBoolean(
                    R.styleable.RateView_editable,
                    DEFAULT_EDITABLE
                )
            } finally {
                recycle()
            }
        }
        rebuildLayout()
    }


    /**
     * Private fields validation.
     */

    private fun validateMaxRating(value: Int) {
        if (value < 1)
            throw Exception("RateView: maxRating has to be more than 0")
    }

    private fun validateRating(value: Int) {
        if (value !in 0..mMaxRating)
            throw Exception("RateView: rating has to be more or equal to 0 and less or equal to maxRating")
    }


    /**
     * Public methods
     */

    fun setMaxRating(value: Int) {
        if (value == mMaxRating) return
        validateMaxRating(value)
        mMaxRating = value
        rebuildLayout()
    }

    fun getMaxRating(): Int {
        return mMaxRating
    }

    fun setRating(value: Int) {
        if (value == mRating) return
        validateRating(value)
        mRating = value
        updateRateSymbols()
        mListener?.onRatingChanged(this, mRating)
    }

    fun getRating(): Int {
        return mRating
    }

    fun setFilledRateSymbol(value: Int) {
        if (value == mFilledRateSymbol) return
        mFilledRateSymbol = value
        updateRateSymbols()
    }

    fun setUnfilledRateSymbol(value: Int) {
        if (value == mUnfilledRateSymbol) return
        mUnfilledRateSymbol = value
        updateRateSymbols()
    }

    fun setOnRatingChangeListener(listener: OnRatingChangeListener) {
        this.mListener = listener
    }


    /**
     * Layout logic
     */

    private fun updateRateSymbols() {
        mRateSymbols.forEachIndexed { index: Int, imageView: ImageView ->
            imageView.setImageResource(if (index+1 <= mRating) mFilledRateSymbol else mUnfilledRateSymbol)
        }
    }

    private fun buildLayout() {
        val newSymbols = mutableListOf<ImageView>()
        for (i in 1..mMaxRating) {
            val symbolView = ImageView(context)
            symbolView.id = generateViewId()
            symbolView.adjustViewBounds = true
            symbolView.layoutParams = ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            symbolView.setOnClickListener {
                if (editable)
                    setRating(
                        if (i == mRating) 0
                        else i
                    )
            }

            addView(symbolView)
            newSymbols.add(symbolView)
        }
        mRateSymbols = newSymbols
        updateRateSymbols()

        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        mRateSymbols.forEach { constraintSet.centerVertically(it.id, ConstraintSet.PARENT_ID) }

        constraintSet.createHorizontalChain(
            ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
            mRateSymbols.map { it.id } .toIntArray(),
            null,
            ConstraintSet.CHAIN_PACKED
        )

        setConstraintSet(constraintSet)
    }

    private fun rebuildLayout() {
        removeAllViewsInLayout()
        buildLayout()
        invalidate()
        requestLayout()
    }


    /**
     * Interface for listener.
     */

    interface OnRatingChangeListener {
        fun onRatingChanged(view: RateView, rating: Int)
    }
}