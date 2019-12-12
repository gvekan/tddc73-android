package com.example.myapplication


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val helloText = MutableLiveData<String>()

    fun setHelloText(value: String) {
        helloText.value = value
    }

    fun getHelloText(): LiveData<String> {
        return helloText
    }
}
