package com.example.xddemo.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.xddemo.XDaoApplication
import com.example.xddemo.ui.viewmodel.DownloadViewModel
import com.example.xddemo.ui.viewmodel.ThreadViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ThreadViewModel(
                repository = xDaoApplication().container.threadRepository,
                userPreferencesRepository = xDaoApplication().userPreferencesRepository
            )
        }

        initializer {
            DownloadViewModel(
                repository = xDaoApplication().container.threadRepository,
            )
        }
    }
}

fun CreationExtras.xDaoApplication(): XDaoApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as XDaoApplication)