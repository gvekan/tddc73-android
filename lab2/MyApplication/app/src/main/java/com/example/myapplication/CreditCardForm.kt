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
import java.util.Calendar
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.InputFilter
import android.view.MotionEvent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.inputmethod.InputMethodManager


class CreditCardForm : FrameLayout {
    var isFront = true
    var currentImage = R.drawable.visa
    var mCardBackLayout :View? = null
    var mCardFrontLayout :View? = null
    var mSetRightOut :AnimatorSet? = null
    var mSetLeftIn :AnimatorSet? = null
    var mSetLeftOut :AnimatorSet? = null
    var mSetRightIn :AnimatorSet? = null
    var focusedEditText: View? = null

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

        // Views to do things with
        val tvNumber = view.findViewById<TextView>(R.id.tv_number)
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCVV = view.findViewById<TextView>(R.id.tv_cvv)
        val tvExpires = view.findViewById<TextView>(R.id.tv_expires)
        val etNumber = view.findViewById<EditText>(R.id.et_number)
        val etName = view.findViewById<EditText>(R.id.et_name)
        val etCVV = view.findViewById<EditText>(R.id.et_cvv)
        val ivLogo = view.findViewById<ImageView>(R.id.iv_logo)

        // Bind number: Format number and change logo depending on number
        etNumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                // Amex has only 15 digits
                if (currentImage == R.drawable.amex && s.length == 16)
                    s.replace(0, s.length, s.subSequence(0, s.length-1))
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (Regex("^(34|37)").containsMatchIn(s)) {
                    tvNumber.text = formatNumberAmex(s)
                    if (currentImage != R.drawable.amex) {
                        currentImage = R.drawable.amex
                        ivLogo.setImageResource(currentImage)
                    }
                } else {
                    tvNumber.text = formatNumber(s)
                    if (Regex("^5[1-5]").containsMatchIn(s)) {
                        if (currentImage != R.drawable.mastercard) {
                            currentImage = R.drawable.mastercard
                            ivLogo.setImageResource(currentImage)
                        }
                    } else if (Regex("^6011").containsMatchIn(s)) {
                        if (currentImage != R.drawable.discover) {
                            currentImage = R.drawable.discover
                            ivLogo.setImageResource(currentImage)
                        }
                    } else if (currentImage != R.drawable.visa){
                        currentImage = R.drawable.visa
                        ivLogo.setImageResource(currentImage)
                    }
                }
            }
        })

        // Bind name
        etName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.length == 0) {
                    tvName.text = resources.getString(R.string.placeholder_tv_name)
                } else {
                    tvName.text = s
                }
            }
        })

        // Bind CVV: Format CVV
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

        // Bind Expire Month
        val spinnerMonth = view.findViewById<Spinner>(R.id.spinner_month)
        val months = mutableListOf("Month")
        for (i in 1..12) {
            var num = i.toString()
            if (num.length == 1) {
                num = "0$num"
            }
            months.add(num)
        }
        val aaMonth = ArrayAdapter(context, android.R.layout.simple_spinner_item, months)
        aaMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = aaMonth
        spinnerMonth.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                var s = parentView.getItemAtPosition(position).toString()
                if (s == "Month") {
                    s = "MM"
                }
                s += tvExpires.text.subSequence(2,5)
                tvExpires.text = s
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })

        // Bind Expire Year
        val spinnerYear = view.findViewById<Spinner>(R.id.spinner_year)
        val years = mutableListOf("Year")
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in currentYear..currentYear+11) {
            years.add(i.toString())
        }
        val aaYear = ArrayAdapter(context, android.R.layout.simple_spinner_item, years)
        aaYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = aaYear
        spinnerYear.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                var s = ""
                s += tvExpires.text.subSequence(0,3)
                var y = parentView.getItemAtPosition(position).toString()
                if (y == "Year") {
                    s += "YY"
                } else {
                    s += y.subSequence(2,4)
                }
                tvExpires.text = s
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        })


        // Animations

        // Setup animation
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

        // Bind Animations: Lambdas
        val etFrontLambda = getEtLambda(forFront = true)
        val etBackLambda = getEtLambda(forFront = false)
        val spinnerFrontLambda: (View, MotionEvent) -> Boolean = {v, event ->
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(focusedEditText?.windowToken, 0)
            // EditText CVV must lose focus so we can get to the back
            if (focusedEditText == etCVV)
                focusedEditText?.clearFocus()
            if (!isFront) flip()
            v.performClick()
        }
        // To front animation
        etName.setOnFocusChangeListener(etFrontLambda)
        etNumber.setOnFocusChangeListener(etFrontLambda)
        spinnerMonth.setOnTouchListener(spinnerFrontLambda)
        spinnerYear.setOnTouchListener(spinnerFrontLambda)
        // To back animation
        etCVV.setOnFocusChangeListener(etBackLambda)
    }

    private fun getEtLambda(forFront: Boolean): (View, Boolean) -> Unit {
        return {v, hasFocus ->
            if (hasFocus) focusedEditText = v
            if (hasFocus && isFront != forFront) flip()
        }
    }

    private fun formatNumber(raw: CharSequence): String{
        var formatted = ""
        for (i in 1..16) {
            if (raw.length < i) {
                formatted += "#"
            } else if (i in 5..12){
                formatted += "*"
            }
            else {
                formatted += raw[i-1]
            }
            if (i%4 == 0) {
                formatted += "     "
            }
        }
        return formatted
    }

    private fun formatNumberAmex(raw: CharSequence): String{
        var formatted = ""
        for (i in 1..15) {
            if (raw.length < i) {
                formatted += "#"
            } else if (i in 5..12){
                formatted += "*"
            }
            else {
                formatted += raw[i-1]
            }
            if (i == 4 || i == 10) {
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