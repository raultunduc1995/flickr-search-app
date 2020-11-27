package com.example.flickrsearchapp.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

fun AppCompatActivity.addFragment(
    containerId: Int,
    fragment: Fragment,
    shouldAddToBackStack: Boolean = false,
    fragmentTag: String? = null
) {
    supportFragmentManager.commit {
        setReorderingAllowed(true)
        add(containerId, fragment)
        if (shouldAddToBackStack && fragmentTag != null)
            addToBackStack(fragmentTag)
    }
}

fun AppCompatActivity.replaceFragment(
    containerId: Int,
    fragment: Fragment,
    shouldAddToBackStack: Boolean = false,
    fragmentTag: String? = null
) {
    supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace(containerId, fragment)
        if (shouldAddToBackStack && fragmentTag != null)
            addToBackStack(fragmentTag)
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.remove() {
    visibility = View.GONE
}