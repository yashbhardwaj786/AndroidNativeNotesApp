package com.epsoftandroid.demo.notifiers

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class Notifier(private val viewModelScope: CoroutineScope) {
    private val flow = MutableStateFlow<Notify>(Notify())
    fun notify(event: Notify, coroutineContext: CoroutineContext = Dispatchers.IO) {
        viewModelScope.launch(coroutineContext) {
            flow.emit(event)
        }
    }

    fun notify(event: Notify, coroutineContext: CoroutineContext = Dispatchers.IO, delayMillis: Long) {
        viewModelScope.launch(coroutineContext) {
            delay(delayMillis)
            flow.emit(event)
        }
    }

    fun receive(coroutineContext: CoroutineContext = Dispatchers.Main, callback: (event: Notify) -> Unit) {
        viewModelScope.launch(coroutineContext) {
            flow.collect { event ->
                callback(event)
            }
        }
    }

    fun close() {
    }
}
