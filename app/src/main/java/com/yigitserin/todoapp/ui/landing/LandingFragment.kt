package com.yigitserin.todoapp.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import com.yigitserin.todoapp.databinding.FragmentLandingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LandingFragment: Fragment() {

    private val landingViewModel: LandingViewModel by viewModels()
    private lateinit var binding: FragmentLandingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        landingViewModel.checkLoginStatus()
        subscribeToOneShotEvents()
    }

    private fun subscribeToOneShotEvents(){
        lifecycleScope.launch {
            viewLifecycleOwner.whenStarted {
                landingViewModel.oneShotUiEvents.collectLatest { event ->
                    when (event) {
                        is LandingViewUIEvent.NavigateToLogin -> findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToLoginFragment())
                        is LandingViewUIEvent.NavigateToList -> findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToListFragment())
                    }
                }
            }
        }
    }
}