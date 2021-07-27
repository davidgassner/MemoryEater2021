package com.example.memoryeater2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.memoryeater2021.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val mData = mutableListOf<String>()
    private var job = Job()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            it.eatButton.setOnClickListener { eatMemory() }
            it.releaseButton.setOnClickListener { releaseMemory() }
        }

        updateDisplay()
    }

    override fun onDestroy() {
        try {
            job.cancel()
        } catch (ignore: UninitializedPropertyAccessException) {
        }
        super.onDestroy()
    }

    private fun eatMemory() {

        // If the current job has already been used, create a new one.
        if (job.isCompleted) {
            job = Job()
        }
        // Create the coroutine scope and launch the code.
        CoroutineScope(job + Dispatchers.Default).launch {

            for (i in 1..1000) {

                // Add 10,000 items to the collection
                for (j in 1..10000) {
                    mData.add("Item $i:$j")
                }
                delay(500)

                // Update the display while in the Main (UI) thread
                withContext(Dispatchers.Main) {
                    updateDisplay()
                }

                // Exit this coroutine if the user has quit the app
                if (job.isCancelled) {
                    println("Leaving the coroutine")
                    break
                }

            }
        }
    }

    private fun releaseMemory() {
        try {
            job.cancel()
        } catch (e: UninitializedPropertyAccessException) {
        }
        mData.clear()
        updateDisplay()
    }

    private fun updateDisplay() {
        val report = "${getString(R.string.report_label)}: ${mData.size}"
        binding.tvOut.text = report
    }
}