package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(),
    FragmentManager.OnBackStackChangedListener,
    MainFragment.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment.newInstance())
                .commitNow()
        }
        supportFragmentManager.addOnBackStackChangedListener(this)
        changeDisplayHomeUp()
    }

    override fun onBackStackChanged() {
        changeDisplayHomeUp()
    }

    private fun changeDisplayHomeUp() {
        val canGoBack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.run { setDisplayHomeAsUpEnabled(canGoBack) }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }

    override fun onItemClick() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, HelloFragment.newInstance())
            addToBackStack(null)
        }.commit()
    }
}
