package com.example.myapplication.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.myapplication.R

/**
 * Used to display details about a repository.
 */
class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var mDescriptionView: TextView
    private lateinit var mLicenseView: TextView
    private lateinit var mCommitsView: TextView
    private lateinit var mBranchesView: TextView
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        mDescriptionView = view.findViewById(R.id.description)
        mLicenseView = view.findViewById(R.id.license)
        mCommitsView = view.findViewById(R.id.commits)
        mBranchesView = view.findViewById(R.id.branches)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        /**
         * Update textViews in the view when selected repository changes.
         */
        viewModel.getRepositoryDetailOfSelected().observe(this, Observer { repositoryDetailOfSelected ->
            if (repositoryDetailOfSelected != null) {
                mDescriptionView.text = repositoryDetailOfSelected.description
                mLicenseView.text = repositoryDetailOfSelected.license
                mCommitsView.text = repositoryDetailOfSelected.commits.toString()
                mBranchesView.text = repositoryDetailOfSelected.branches.toString()
            } else {
                mDescriptionView.text = ""
                mLicenseView.text = ""
                mCommitsView.text = ""
                mBranchesView.text = ""
            }
        })

    }

}
