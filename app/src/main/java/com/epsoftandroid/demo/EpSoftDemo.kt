package com.epsoftandroid.demo

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Bundle
import com.epsoftandroid.demo.data.db.ObjectBox
import com.epsoftandroid.demo.ui.viewmodelfactory.AddNotesViewModelFactory
import com.epsoftandroid.demo.ui.viewmodelfactory.MainViewModelFactory
import io.objectbox.BoxStore
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class EpSoftDemo :
    Application(),
    KodeinAware,
    Application.ActivityLifecycleCallbacks,
    ComponentCallbacks2 {

    operator fun get(context: Context): EpSoftDemo {
        return context.applicationContext as EpSoftDemo
    }

    companion object {
        const val TAG = "ObjectBoxExample"
        lateinit var boxStore: BoxStore
            private set
        private lateinit var application: EpSoftDemo
        @JvmStatic
        fun getInstance(): EpSoftDemo {
            return application
        }

    }

    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)

    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@EpSoftDemo))
        bind() from provider { MainViewModelFactory() }
        bind() from provider { AddNotesViewModelFactory() }
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onActivityStarted(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityResumed(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityPaused(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(p0: Activity) {
        TODO("Not yet implemented")
    }
}