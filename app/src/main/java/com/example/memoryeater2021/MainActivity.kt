package com.example.memoryeater2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.memoryeater2021.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            it.eatButton.setOnClickListener {
                viewModel.eatMemory()
            }
            it.releaseButton.setOnClickListener {
                viewModel.releaseMemory()
            }
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java).also {
            it.memoryMessage.observe(this, { message ->
                binding.tvOut.text = message
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releaseMemory()
    }

}