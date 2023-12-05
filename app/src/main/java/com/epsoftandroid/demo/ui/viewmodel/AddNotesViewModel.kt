package com.epsoftandroid.demo.ui.viewmodel

import androidx.databinding.ObservableField
import com.epsoftandroid.demo.common.BaseViewModel
import com.epsoftandroid.demo.notifiers.Notify

class AddNotesViewModel(): BaseViewModel() {

    var errorTitleMessage = ObservableField("")
    var isTitleError = ObservableField(false)
    var errorDescriptionMessage = ObservableField("")
    var isDescriptionMessageError = ObservableField(false)


    companion object {
        const val SAVE_BUTTON_CLICK = "SAVE_BUTTON_CLICK"
    }

}