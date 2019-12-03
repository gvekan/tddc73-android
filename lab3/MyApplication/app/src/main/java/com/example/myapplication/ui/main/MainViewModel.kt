package com.example.myapplication.ui.main

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.DataRepository
import android.os.Handler


class MainViewModel : ViewModel() {

    private val dataRepository = DataRepository()
    private var language = ""
    private val repositories: MutableLiveData<List<RepositoryItem>> = MutableLiveData()
    /*by lazy {
        MutableLiveData<List<RepositoryItem>>().also {
            loadRepositories()
        }
    }*/
    private val repositoryDetailOfSelected: MutableLiveData<RepositoryDetail> = MutableLiveData()
    private val isLoading = MutableLiveData<Boolean>().apply { value = false }

    init {
        loadRepositories()
    }


    fun getRepositories(): LiveData<List<RepositoryItem>> {
        return repositories
    }

    fun getRepositoryDetailOfSelected(): LiveData<RepositoryDetail> {
        return repositoryDetailOfSelected
    }

    fun setLanguage(lang: String) {
        if (lang != language) {
            language = lang
            repositories.value = listOf()
            loadRepositories()
        }
    }

    fun getIsLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun select(owner: String, name: String) {
        repositoryDetailOfSelected.value = null
        loadRepositoryDetailOfSelected(owner, name)
    }


    private fun loadRepositories() {
        isLoading.value = true
        Thread(Runnable{
            dataRepository.getMostStarredRepositories( { r ->
                Handler(Looper.getMainLooper()).post{
                    repositories.value = r
                    isLoading.value = false
                }
            }, language)
        }).start()
    }

    private fun loadRepositoryDetailOfSelected(owner: String, name: String) {
        isLoading.value = true
        Thread(Runnable{
            dataRepository.getRepository( { r ->
                Handler(Looper.getMainLooper()).post{
                    repositoryDetailOfSelected.value = r
                    isLoading.value = false
                }
            }, owner, name)
        }).start()
    }


    data class RepositoryItem(
        val name: String,
        val owner: String,
        val description: String?,
        val forks: Int,
        val stars: Int
    )

    data class RepositoryDetail(
        val description: String?,
        val license: String?,
        val commits: Int,
        val branches: Int
    )
}
