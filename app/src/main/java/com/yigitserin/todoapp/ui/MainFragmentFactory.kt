package com.yigitserin.todoapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.yigitserin.todoapp.ui.list.ListAdapter
import com.yigitserin.todoapp.ui.list.ListFragment
import javax.inject.Inject

class MainFragmentFactory @Inject constructor(
    private val listAdapter: ListAdapter
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            ListFragment::class.java.name -> ListFragment(listAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}