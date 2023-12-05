package com.epsoftandroid.demo.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.epsoftandroid.demo.common.BaseActivity
import com.epsoftandroid.demo.common.BaseViewModel
import com.epsoftandroid.demo.ui.viewmodelfactory.MainViewModelFactory
import com.epsoftandroid.demo.R
import com.epsoftandroid.demo.common.ModuleMaster
import com.epsoftandroid.demo.data.db.ObjectBox
import com.epsoftandroid.demo.data.model.Note
import com.epsoftandroid.demo.databinding.ActivityNotesListBinding
import com.epsoftandroid.demo.notifiers.Notify
import com.epsoftandroid.demo.ui.adapter.NotesItemAdapter
import com.epsoftandroid.demo.ui.viewmodel.MainViewModel
import com.epsoftandroid.demo.utils.showToast
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : BaseActivity() {

    override val kodein by kodein()
    private lateinit var mainViewModel: MainViewModel
    private val factory by instance<MainViewModelFactory>()
    private val binding: ActivityNotesListBinding by lazyBinding()
    override val dataBinding: Boolean = true
    private lateinit var notesBox: Box<Note>
    private var mLayoutManager: LinearLayoutManager? = null
    override val layoutResource: Int = R.layout.activity_notes_list
    override fun getViewModel(): BaseViewModel {
        return mainViewModel
    }

    override fun initializeViewModel() {
        mainViewModel =
            ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    override fun setBindings() {
        binding.viewModel = mainViewModel
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNotificationReceived(data: Notify) {
        when(data.identifier){
            MainViewModel.ADD_NOTE_CLICK -> {
                ModuleMaster.navigateToAddNoteActivity(this, true)
            }

            MainViewModel.NOTE_CLICK -> {
                val itemId = data.arguments[0] as Long
                ModuleMaster.navigateToAddNoteActivity(this, false, itemId)
            }
            MainViewModel.REMOVE_CLICK -> {
                val itemId = data.arguments[0] as Long
                if(itemId.toInt() != -1) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val removed = notesBox.remove(itemId)
                        lifecycleScope.launch(Dispatchers.Main){
                            showToast(this@MainActivity, "Record removed successfully")
                        }
                    }
                }
            }

        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBar("My Notes")
        initRecyclerView()
        if (ObjectBox.dbExceptionMessage != null) {
            showToast(this@MainActivity, "Error in Database")
            return
        }
        notesBox = ObjectBox.boxStore.boxFor()
        ObjectBox.notesLiveData.observe(this, Observer {
            (binding.notesList.adapter as NotesItemAdapter).setNotes(it)
        })
    }

    private fun initRecyclerView() {
        binding.notesList.apply {
            mLayoutManager = LinearLayoutManager(context)
            layoutManager = mLayoutManager
            adapter = NotesItemAdapter(mainViewModel)
        }
    }
}