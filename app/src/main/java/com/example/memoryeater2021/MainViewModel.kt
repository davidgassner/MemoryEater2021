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
    val report = MutableLiveData<String>()
    private var isCoroutineRunning = false

    /**
     * Add items to the data collection to chew up available system memory
     */
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

    /**
     * Clear the data collection and break the coroutine's loop
     */
    fun releaseMemory() {
        isCoroutineRunning = false
        data.clear()
        reportDataSize()
    }

    /**
     * Post a new message to the live data object that the main activity is observing
     */
    private fun reportDataSize() {
        report.postValue("Number of items: ${data.size}")
    }

}