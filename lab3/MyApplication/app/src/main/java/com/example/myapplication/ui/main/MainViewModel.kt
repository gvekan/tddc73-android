package com.example.myapplication.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class MainViewModel : ViewModel() {
    private val repositories: MutableLiveData<List<RepositoryItem>> = MutableLiveData()
    /*by lazy {
        MutableLiveData<List<RepositoryItem>>().also {
            loadRepositories()
        }
    }*/

    init {
        loadRepositories()
    }

    fun getRepositories(): LiveData<List<RepositoryItem>> {
        return repositories
    }

    private fun loadRepositories() {
        repositories.value = (1..50).map { i -> RepositoryItem("Repo $i") }
    }

    data class RepositoryItem(val name: String)

    data class RepositoryDetail(val name: String)
}
