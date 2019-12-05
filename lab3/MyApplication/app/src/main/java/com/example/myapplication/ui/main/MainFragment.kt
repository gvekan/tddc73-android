package com.example.myapplication.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R

/**
 * Used to display a list of repositories and filter options in the form of languages.
 */
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }


    private var listener: MainActivity? = null
    private lateinit var myAdapter: MyItemRecyclerViewAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        // Set up list
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        myAdapter = MyItemRecyclerViewAdapter(listOf(), listener)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = myAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        // Set up language dropdown from resource
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        ArrayAdapter.createFromResource(
            context,
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = listener

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        // Replace items in list when repositories in the view model changes.
        viewModel.getRepositories().observe(this, Observer { repositories ->
            myAdapter.replaceItems(repositories)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * Interface for listener to click on item in list.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: MainViewModel.RepositoryItem)
    }

}
