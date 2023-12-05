package com.epsoftandroid.demo.common

import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.epsoftandroid.demo.utils.Utility
import com.epsoftandroid.demo.notifiers.Loader
import com.epsoftandroid.demo.notifiers.Notify
import com.epsoftandroid.demo.notifiers.NotifyException
import com.epsoftandroid.demo.notifiers.NotifyRetry
import com.epsoftandroid.demo.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class BaseActivity  : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private lateinit var baseBinding: ViewDataBinding
    var toolbar: Toolbar? = null
    var title: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!dataBinding) {
            setContentView(layoutResource)
        } else {
            baseBinding = DataBindingUtil.setContentView(this, layoutResource)
        }

        initializeViewModel()
        setBindings()
        getViewModel()?.let {
            it.notifier.receive { event ->
                when (event) {
                    is NotifyException -> {
                        event.exception.message?.let { msg ->
                        }
                    }
                    is Loader -> {
                        if (event.loading) {
                            Utility.showProgressDialog(this)
                        } else {
                            Utility.hideProgressDialog()
                        }
                    }
                    is NotifyRetry -> {
                    }
                    else -> {
                        onNotificationReceived(event)
                    }
                }
            }
        }


    }

    fun getBinding(): ViewDataBinding {
        return baseBinding
    }

    abstract val dataBinding: Boolean
    abstract val layoutResource: Int
    abstract fun getViewModel(): BaseViewModel?
    abstract fun onNotificationReceived(data: Notify)
    abstract fun setBindings()
    abstract fun initializeViewModel()

    inline fun <reified T> lazyBinding(): Lazy<T> = lazy { getBinding() as T }

    fun setToolBar(titleText: String, isBack: Boolean = false) {
        toolbar = findViewById(R.id.toolbar)
        title = findViewById(R.id.title)
        title?.text = titleText
        title?.maxLines = 1
        title?.ellipsize = TextUtils.TruncateAt.END
        toolbar?.contentInsetStartWithNavigation = 0
        setSupportActionBar(toolbar)
        if (isBack){
            toolbar?.setNavigationIcon(R.drawable.ic_chevron_left)
            toolbar?.setNavigationOnClickListener { finish() }
        }

    }
}