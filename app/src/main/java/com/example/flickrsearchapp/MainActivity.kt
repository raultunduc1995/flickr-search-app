package com.example.flickrsearchapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flickrsearchapp.databinding.ActivityMainBinding
import com.example.flickrsearchapp.fragments.MainFragment
import com.example.flickrsearchapp.utils.addFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        if (savedInstanceState == null)
            addFragment(R.id.fragments_container, MainFragment())
    }

    private fun initToolbar() {
        binding.myToolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(binding.myToolbar)
    }
}