package com.yigitserin.todoapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import com.yigitserin.todoapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment :Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        subscribeToOneShotEvents()
        setViews()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launch {
            loginViewModel.uiState.collectLatest { state ->
                binding.progressBar.visibility = when (state.isLoading) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            }
        }
    }

    private fun subscribeToOneShotEvents(){
        lifecycleScope.launch {
            viewLifecycleOwner.whenStarted {
                loginViewModel.oneShotUiEvents.collectLatest { event ->
                    when (event) {
                        is LoginViewUIEvent.NavigateToList -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToListFragment())
                        is LoginViewUIEvent.ShowToast -> Toast.makeText(context, getString(event.messageId), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setViews(){
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                loginViewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            }
        }
    }
}