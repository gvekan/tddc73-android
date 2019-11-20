package com.example.myapplication

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class CreditCardForm : FrameLayout {
    var isFront = true
    var mCardBackLayout :View? = null
    var mCardFrontLayout :View? = null
    var mSetRightOut :AnimatorSet? = null
    var mSetLeftIn :AnimatorSet? = null
    var mSetLeftOut :AnimatorSet? = null
    var mSetRightIn :AnimatorSet? = null

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
        val view = LayoutInflater.from(context).inflate(R.layout.credit_card_form, this, true)

        // Bind and validate input
        val tvNumber = view.findViewById<TextView>(R.id.tv_number)
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCVV = view.findViewById<TextView>(R.id.tv_cvv)
        val etNumber = view.findViewById<EditText>(R.id.et_number)
        val etName = view.findViewById<EditText>(R.id.et_name)
        val etCVV = view.findViewById<EditText>(R.id.et_cvv)
        etNumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                tvNumber.text = formatNumber(s)
                //TODO: Change bank logo
            }
        })
        etName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (count == 0) {
                    tvName.text = resources.getString(R.string.placeholder_tv_name)
                } else {
                    tvName.text = s
                }
            }
        })
        etCVV.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                tvCVV.text = "*".repeat(s.length)
            }
        })

        val spinnerMonth = view.findViewById<Spinner>(R.id.spinner_month)
        val months = arrayOf("Month", "01", "02")
        val aaMonth = ArrayAdapter(context, android.R.layout.simple_spinner_item, months)
        aaMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = aaMonth


        // Flip animations
        mCardBackLayout = view.findViewById(R.id.card_back)
        mCardFrontLayout = view.findViewById(R.id.card_front)


        mSetRightOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out) as AnimatorSet
        mSetLeftIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in) as AnimatorSet
        mSetLeftOut = AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out) as AnimatorSet
        mSetRightIn = AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in) as AnimatorSet

        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        mCardFrontLayout?.cameraDistance = scale
        mCardBackLayout?.cameraDistance = scale

        etName.setOnFocusChangeListener {v, hasFocus -> if (hasFocus && !isFront) flip()}
        etNumber.setOnFocusChangeListener {v, hasFocus -> if (hasFocus && !isFront) flip()}
        etCVV.setOnFocusChangeListener {v, hasFocus -> if (hasFocus && isFront) flip()}
    }

    private fun formatNumber(raw: CharSequence): String{
        var formatted = " "
        for (i in 0..15) {
            if (raw.length - 1 < i) {
                formatted += "#"
            } else if (i in 4..11){
                formatted += "*"
            }
            else {
                formatted += raw[i]
            }
            if ((i+1)%4 == 0) {
                formatted += "     "
            }
        }
        return formatted

    }

    private fun flip() {
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
    }
}