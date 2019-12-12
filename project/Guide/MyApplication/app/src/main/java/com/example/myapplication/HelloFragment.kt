package com.example.myapplication

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer


class HelloFragment : Fragment() {

    companion object {
        fun newInstance() = HelloFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hello_fragment, container, false).also {
            textView = it.findViewById(R.id.textView)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity!!.run { ViewModelProviders.of(this).get(MainViewModel::class.java) }
        viewModel.getHelloText().observe(this, Observer {helloString ->
            textView.text = helloString
        })
    }

}
