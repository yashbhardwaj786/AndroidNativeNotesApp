package com.epsoftandroid.demo.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epsoftandroid.demo.ui.viewmodel.AddNotesViewModel
import com.epsoftandroid.demo.ui.viewmodel.MainViewModel

class AddNotesViewModelFactory() : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  AddNotesViewModel() as T
    }
}