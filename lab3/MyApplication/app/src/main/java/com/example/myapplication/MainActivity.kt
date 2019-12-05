package com.example.myapplication

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.ui.main.DetailFragment
import com.example.myapplication.ui.main.MainFragment
import com.example.myapplication.ui.main.MainViewModel

/**
 * Handles navigation between MainFragment and DetailFragment.
 * Implements logic to display a loader when necessary.
 * Handles item clicks in list and spinner in MainFragment.
 */
class MainActivity : AppCompatActivity(),
    MainFragment.OnListFragmentInteractionListener,
    FragmentManager.OnBackStackChangedListener,
    AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
            supportFragmentManager.addOnBackStackChangedListener(this)
        }
        changeDisplayHomeUp()

        // Update progress bar depending on if view model is loading.
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.getIsLoading().observe(this, Observer { isLoading ->
            if (isLoading)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.INVISIBLE
        })
    }

    /**
     * Change to DetailFragment when item in list is selected.
     */
    override fun onListFragmentInteraction(item: MainViewModel.RepositoryItem) {
        viewModel.select(item.owner, item.name)
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, DetailFragment.newInstance())
            addToBackStack(null)
        }
        transaction.commit()
        supportActionBar?.title = item.name
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Change language in view model when a language is selected.
     * Could also be implemented in MainFragment.
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val lang = parent?.getItemAtPosition(position).toString()
        if (lang == resources.getStringArray(R.array.languages).first())
            viewModel.setLanguage("")
        else
            viewModel.setLanguage(lang)
    }

    override fun onBackStackChanged() {
        changeDisplayHomeUp()
    }

    /**
     * Logic to decide if a up button should be displayed in the ActionBar.
     */
    private fun changeDisplayHomeUp() {
        val canGoBack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(canGoBack)
        }?: throw Exception("Invalid ActionBar")
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        supportActionBar?.title = getString(R.string.app_name)
        return true
    }

}
