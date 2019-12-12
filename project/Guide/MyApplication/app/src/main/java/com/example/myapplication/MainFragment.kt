package com.example.myapplication

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


class MainFragment : Fragment(), AdapterView.OnItemClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private val items = listOf("Hello", "Bonjour", "Guten Tag", "Hej", "Ave")
    private lateinit var editText: EditText
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        view.findViewById<ListView>(R.id.listView).apply {
            adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items)
            onItemClickListener = this@MainFragment
        }
        editText = view.findViewById(R.id.editText)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity!!.run { ViewModelProviders.of(this).get(MainViewModel::class.java) }
        // TODO: Use the ViewModel
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.setHelloText("${items[position]} ${editText.text}")
        onItemClickListener?.onItemClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemClickListener)
            onItemClickListener = context
    }

    override fun onDetach() {
        super.onDetach()
        onItemClickListener = null
    }

    interface OnItemClickListener {
        fun onItemClick()
    }
}
