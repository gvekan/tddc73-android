package com.example.mylibrary

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import kotlin.math.min

/**
 * A ViewGroup that displays some children at a time.
 */
class CarouselView : ViewGroup, CarouselViewAdapter.DataSetChangeListener {


    /**
     * Default attributes values.
     */

    val DEFAULT_ITEMS_TO_DISPLAY = 3
    val DEFAULT_SPACE_BETWEEN_ITEMS = 8
    val DEFAULT_ITEMS_TO_JUMP = 1
    val DEFAUL_SHOW_RADIO_BUTTONS = true


    /**
     * Private attributes fields
     */

    private var mItemsToDisplay: Int = DEFAULT_ITEMS_TO_DISPLAY
    private var mItemsToJump: Int = DEFAULT_ITEMS_TO_JUMP
    private var mSpaceBetweenItems: Int = DEFAULT_SPACE_BETWEEN_ITEMS
    private var mShowRadioButtons: Boolean = DEFAUL_SHOW_RADIO_BUTTONS


    /**
     * Other private fields.
     */

    private var mListener: OnFirstDisplayItemIndexChangeListener? = null
    private var mAdapter: CarouselViewAdapter? = null
    private var mFirstDisplayItemIndex: Int = 0
        set(value) {
            field = value
            mListener?.onFirstDisplayItemIndexChanged(this, value)
        }
    private var mItemCount: Int = 0
    private var mItems = mutableListOf<View>()
    private var mItemWidth: Int = 0
    private var mItemHeight: Int = 0
    private var mRadioButtons: RadioGroup? = null
    private var mRadioButtonsLeftConstraint: Int = 0
    private var mRadioButtonsWidth: Int = 0
    private var mRadioButtonsHeight: Int = 0


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
            R.styleable.CarouselView,
            0, 0)?.apply {

            try {
                mItemsToDisplay = getInteger(
                    R.styleable.CarouselView_itemsToDisplay,
                    DEFAULT_ITEMS_TO_DISPLAY
                ).also { validateItemsToDisplay(it) }
                mItemsToJump = getInteger(
                    R.styleable.CarouselView_itemsToJump,
                    DEFAULT_ITEMS_TO_JUMP
                ).also { validateItemsToJump(it) }
                mSpaceBetweenItems = getInteger(
                    R.styleable.CarouselView_spaceBetweenItems,
                    DEFAULT_SPACE_BETWEEN_ITEMS
                ).also { validateSpaceBetweenItems(it) }
                mShowRadioButtons =getBoolean(
                    R.styleable.CarouselView_showRadioButtons,
                    DEFAUL_SHOW_RADIO_BUTTONS
                )
            } finally {
                recycle()
            }
        }
        this.setOnTouchListener(object: OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                jumpForward()
            }

            override fun onSwipeRight() {
                jumpBackward()
            }
        })
        buildLayout()
    }


    /**
     * Private fields validation.
     */

    private fun validateItemsToDisplay(value: Int) {
        if (value < 1)
            throw Exception("CarouselView: itemsToDisplay has to be more than 0")
    }

    private fun validateItemsToJump(value: Int) {
        if (value < 0 || value > mItemsToDisplay)
            throw Exception("CarouselView: ItemsToJump has to be more than 0 and less or equal to itemsToDisplay")

    }

    private fun validateSpaceBetweenItems(value: Int) {
        if (value < 1)
            throw Exception("CarouselView: spaceBetweenItems has to be more than 0")
    }

    private fun validateFirstDisplayItemIndex(value: Int) {
        if (value < 0 || value > mItemCount-1 || value % mItemsToJump != 0)
            throw Exception("CarouselView: FirstDisplayItemIndex has to be more than 0 and less " +
                    "or equal to the number of items and a value that works with itemsToJump")
    }


    /**
     * Public methods
     */

    fun setItemsToDisplay(value: Int) {
        if (value == mItemsToDisplay) return
        validateItemsToDisplay(value)
        mItemsToDisplay = value
        if (mItemsToJump > value)
            mItemsToJump = value
        mFirstDisplayItemIndex = 0
        rebuildLayout()
    }

    fun getItemsToDisplay(): Int {
        return mItemsToDisplay
    }

    fun setItemsToJump(value: Int) {
        if (value == mItemsToJump) return
        validateItemsToJump(value)
        mItemsToDisplay = value
        mFirstDisplayItemIndex = 0
        rebuildLayout()
    }

    fun getItemsToJump(): Int {
        return mItemsToJump
    }

    fun setSpaceBetweenItems(value: Int) {
        if (value == mSpaceBetweenItems) return
        validateSpaceBetweenItems(value)
        mSpaceBetweenItems = value
        rebuildItems()
    }

    fun getSpaceBetweenItems(): Int {
        return mSpaceBetweenItems
    }

    fun setShowRadioButtons(value: Boolean) {
        if (value == mShowRadioButtons) return
        mShowRadioButtons = value
        if (mShowRadioButtons)
            buildRadioButtons()
        else {
            removeView(mRadioButtons)
            mRadioButtons = null
        }
    }

    fun getShowRadioButtons(): Boolean {
        return mShowRadioButtons
    }

    fun setFirstDisplayItemIndex(value: Int) {
        if (value == mFirstDisplayItemIndex) return
        validateFirstDisplayItemIndex(value)
        mFirstDisplayItemIndex = value
        val radioButton = mRadioButtons?.getChildAt(mFirstDisplayItemIndex/mItemsToJump) as RadioButton
        radioButton.isChecked = true
        rebuildItems()
    }

    fun getFirstDisplayItemIndex(): Int {
        return mFirstDisplayItemIndex
    }

    fun setAdapter(adapter: CarouselViewAdapter) {
        mAdapter?.unregisterListener()

        mAdapter = adapter
        mAdapter?.registerListener(this)

        mItemCount = adapter.getItemCount()
        rebuildLayout()
    }

    fun setOnFirstDisplayItemIndexChangeListener(listener: OnFirstDisplayItemIndexChangeListener) {
        mListener = listener
    }

    fun jumpForward() {
        if (mFirstDisplayItemIndex+mItemsToDisplay < mItemCount)
            setFirstDisplayItemIndex(mFirstDisplayItemIndex+mItemsToJump)
    }

    fun jumpBackward() {
        setFirstDisplayItemIndex(mFirstDisplayItemIndex-mItemsToJump)
    }


    /**
     * Listener interface methods
     */

    override fun onDataSetChanged() {
        mAdapter?.let {
            if (mItemCount != it.getItemCount()) {
                mItemCount = it.getItemCount()
                mFirstDisplayItemIndex = 0
            }
        }
        rebuildLayout()
    }

    override fun onDataSetChanged(position: Int) {
        if (position !in mFirstDisplayItemIndex..(mFirstDisplayItemIndex+mItemsToDisplay)) return

        rebuildLayout()
    }


    /**
     * Layout logic
     */

    private fun buildItems() {
        for (i in mFirstDisplayItemIndex until min((mFirstDisplayItemIndex+mItemsToDisplay), mItemCount)) {
            mAdapter?.let { adapter ->
                val view = adapter.onCreateView(this, i)
                mItems.add(view)
                addView(view)
            }
        }
    }

    private fun buildRadioButtons() {
        mRadioButtons = RadioGroup(context)
        mRadioButtons!!.orientation = RadioGroup.HORIZONTAL
        mRadioButtons!!.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val checkedIndex = mFirstDisplayItemIndex/mItemsToJump
        for (i in 0 until mItemCount/mItemsToJump) {
            val radioButton = RadioButton(context)
            radioButton.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            mRadioButtons!!.addView(radioButton)
            radioButton.setOnClickListener {
                setFirstDisplayItemIndex(i*mItemsToJump)
            }
            if (i == checkedIndex)
                radioButton.isChecked = true
        }
        addView(mRadioButtons)
    }

    private fun rebuildItems() {
        mItems.forEach{ removeViewInLayout(it) }
        mItems = mutableListOf()
        buildItems()
        invalidate()
        requestLayout()
    }

    private fun rebuildLayout() {
        removeAllViewsInLayout()
        mItems = mutableListOf()
        buildLayout()
        invalidate()
        requestLayout()
    }

    private fun buildLayout() {
        buildItems()
        if (mShowRadioButtons)
            buildRadioButtons()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mItemWidth = 0
        mItemHeight = 0
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        var wSize = MeasureSpec.getSize(widthMeasureSpec)
        var hSize = MeasureSpec.getSize(heightMeasureSpec)
        val wItemConstrains = paddingLeft + paddingRight + mSpaceBetweenItems*(mItemsToDisplay-1)
        var hItemConstrains = paddingTop + paddingBottom

        mRadioButtons?.let {
            val buttonsWidth = wSize - paddingLeft - paddingRight
            val buttonsHeight = (hSize - paddingTop - paddingBottom)
            val buttonsWidthMeasureSpec = MeasureSpec.makeMeasureSpec(buttonsWidth, MeasureSpec.AT_MOST)
            val buttonsHeightMeasureSpec = MeasureSpec.makeMeasureSpec(buttonsHeight, MeasureSpec.AT_MOST)
            it.measure(buttonsWidthMeasureSpec, buttonsHeightMeasureSpec)


            mRadioButtonsWidth = it.measuredWidth
            mRadioButtonsHeight = it.measuredHeight

            hItemConstrains += mRadioButtonsHeight



            Log.i("#######################", "hSize: $hSize, hItemConstrains: $hItemConstrains, hButton: $mRadioButtonsHeight")
            Log.i("#######################", "wSize: $wSize, wItemConstrains: $wItemConstrains, hButton: $mRadioButtonsWidth")
            mRadioButtonsLeftConstraint = (wSize - paddingLeft - paddingRight - mRadioButtonsWidth)/2 + paddingLeft
        }

        val itemWidth = (wSize - wItemConstrains)/mItemsToDisplay
        val itemHeight = hSize - hItemConstrains
        val itemWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWidth, wMode)
        val itemHeightMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight, hMode)

        mItems.forEach {view ->
            view.measure(itemWidthMeasureSpec, itemHeightMeasureSpec)
            if (view.measuredWidth > mItemWidth)
                mItemWidth = view.measuredWidth
            if (view.measuredHeight > mItemHeight)
                mItemHeight = view.measuredHeight
        }

        wSize = wItemConstrains + mItemWidth*mItemsToDisplay
        hSize = hItemConstrains + mItemHeight

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(wSize, wMode),
            MeasureSpec.makeMeasureSpec(hSize, hMode)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var lItem = l + paddingLeft
        val bItem = t + mItemHeight
        mItems.forEach {view ->
            view.layout(lItem, t, lItem+mItemWidth, bItem)
            lItem += mItemWidth + mSpaceBetweenItems
        }
        mRadioButtons?.apply {
            val mL = l + mRadioButtonsLeftConstraint
            layout(mL, bItem, mL + mRadioButtonsWidth, bItem + mRadioButtonsHeight)
        }
    }


    /**
     * Interface for listener.
     */

    interface OnFirstDisplayItemIndexChangeListener {
        fun onFirstDisplayItemIndexChanged(view: CarouselView, index: Int)
    }
}