package com.epsoftandroid.demo.ui.viewmodel

import androidx.databinding.ObservableField
import com.epsoftandroid.demo.common.BaseViewModel
import com.epsoftandroid.demo.notifiers.Notify

class MainViewModel(): BaseViewModel() {

    val isListEmpty = ObservableField(true)

    fun addNoteClick() {
        notifier.notify(Notify(ADD_NOTE_CLICK, ""))
    }

    fun itemClick(itemId: Long){
        notifier.notify(Notify(NOTE_CLICK, itemId))
    }

    fun removeDataFromList(itemId: Long) {
        notifier.notify(Notify(REMOVE_CLICK, itemId))
    }

    companion object {
        const val ADD_NOTE_CLICK = "ADD_NOTE_CLICK"
        const val NOTE_CLICK = "NOTE_CLICK"
        const val REMOVE_CLICK = "REMOVE_CLICK"
    }

}