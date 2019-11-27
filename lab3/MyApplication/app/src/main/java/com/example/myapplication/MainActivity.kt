package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.ui.main.MainFragment
import com.example.myapplication.ui.main.MainViewModel

class MainActivity : AppCompatActivity(), MainFragment.OnListFragmentInteractionListener {

    override fun onListFragmentInteraction(item: MainViewModel.RepositoryItem) {
        Toast.makeText(this, "${item.name} was selected", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
