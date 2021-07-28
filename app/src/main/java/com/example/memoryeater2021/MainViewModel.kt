package com.example.memoryeater2021

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        // Prevent running twice simultaneously
        if (isCoroutineRunning) {
            return
        }

        // Set running flag
        isCoroutineRunning = true

        // Commence eating memory
        viewModelScope.launch(Dispatchers.Default) {

            for (i in 1..1000) {

                // Add 10,000 items to the collection
                for (j in 1..10000) {
                    data.add("Item $i:$j")
                }
                delay(500)

                // Update the display while in the Main (UI) thread
                reportDataSize()

                // Exit this coroutine if memory has been released
                if (!isCoroutineRunning) {
                    break
                }

            }
        }
    }

    /**
     * Post a new message to the live data object that the main activity is observing
     */
    private fun reportDataSize() {
        report.postValue("Number of items: ${data.size}")
    }

    /**
     * Clear the data collection and break the coroutine's loop
     */
    fun releaseMemory() {
        isCoroutineRunning = false
        data.clear()
        reportDataSize()
    }

}