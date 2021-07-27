package com.example.memoryeater2021

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    // data is stored here
    private val data = mutableListOf<String>()

    // live data object to report current data usage
    val memoryMessage = MutableLiveData<String>()
    private var isCoroutineRunning = false

    fun eatMemory() {

        isCoroutineRunning = true

        // Create the coroutine scope and launch the code.
        viewModelScope.launch(Dispatchers.Default) {

            Log.d(TAG, "eatMemory: starting scope")

            for (i in 1..1000) {

                // Add 10,000 items to the collection
                for (j in 1..10000) {
                    data.add("Item $i:$j")
                }
                delay(500)

                // Update the display while in the Main (UI) thread
                reportDataSize()
                Log.d(TAG, "view model running")

                // Exit this coroutine if the coroutine is cancelled
                if (!isCoroutineRunning) {
                    println("Leaving the coroutine")
                    break
                }

            }
        }
    }

    fun releaseMemory() {
        isCoroutineRunning = false
        data.clear()
        reportDataSize()
    }

    private fun reportDataSize() {
        memoryMessage.postValue("Number of items: ${data.size}")
    }

}