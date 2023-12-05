package com.epsoftandroid.demo.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.epsoftandroid.demo.R
import com.epsoftandroid.demo.common.BaseActivity
import com.epsoftandroid.demo.common.BaseViewModel
import com.epsoftandroid.demo.data.db.ObjectBox
import com.epsoftandroid.demo.data.model.Note
import com.epsoftandroid.demo.databinding.ActivityAddNoteBinding
import com.epsoftandroid.demo.databinding.ActivityNotesListBinding
import com.epsoftandroid.demo.notifiers.Notify
import com.epsoftandroid.demo.ui.viewmodel.AddNotesViewModel
import com.epsoftandroid.demo.ui.viewmodel.MainViewModel
import com.epsoftandroid.demo.ui.viewmodelfactory.AddNotesViewModelFactory
import com.epsoftandroid.demo.ui.viewmodelfactory.MainViewModelFactory
import com.epsoftandroid.demo.utils.Constant
import com.epsoftandroid.demo.utils.Constant.Companion.EXTRA_NOTE_ID
import com.epsoftandroid.demo.utils.setButtonActivatedWithColor
import com.epsoftandroid.demo.utils.setButtonDeactivated
import com.epsoftandroid.demo.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.Date

class AddNoteActivity : BaseActivity() {
    private var isAddNotes = false
    private var existingNote: Note? =  null
    override val kodein by kodein()
    private lateinit var addNotesViewModel: AddNotesViewModel
    private val factory by instance<AddNotesViewModelFactory>()
    private val binding: ActivityAddNoteBinding by lazyBinding()
    override val dataBinding: Boolean = true
    private lateinit var descTextError: TextView
    private lateinit var titleTextError: TextView
    private lateinit var saveBtn: Button
    private lateinit var titleEdit: EditText
    private lateinit var description: EditText
    private var isTitleValid = false
    private var isDescriptionValid = false
    override val layoutResource: Int = R.layout.activity_add_note
    override fun getViewModel(): BaseViewModel {
        return addNotesViewModel
    }

    override fun initializeViewModel() {
        addNotesViewModel =
            ViewModelProvider(this, factory)[AddNotesViewModel::class.java]
    }

    override fun setBindings() {
        binding.viewModel = addNotesViewModel
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNotificationReceived(data: Notify) {
        when (data.identifier) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        saveBtn = findViewById(R.id.save)
        titleEdit = findViewById(R.id.titleEdit)
        description = findViewById(R.id.description)
        titleTextError = findViewById(R.id.titleTextError)
        descTextError = findViewById(R.id.descTextError)

        val noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1)

        intent.getBooleanExtra("IS_ADD_NOTES", false).let {
            isAddNotes = it
        }
        if (isAddNotes) {
            setToolBar("Add Notes", true)
        } else {
            setToolBar("Edit Notes", true)
        }
        checkValidations()
        editTextFocusLostListener(titleEdit, TITLE_NAME)
        editTextFocusLostListener(description, DESCRIPTION_NAME)
        editTextChangeListener(titleEdit, TITLE_NAME)
        editTextChangeListener(description, DESCRIPTION_NAME)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                // Get list of Author Objects ordered by name.
                existingNote = withContext(Dispatchers.IO) {
                    if (noteId != -1L) {
                        ObjectBox.boxStore.boxFor(Note::class.java)[noteId]
                    } else {
                        null
                    }
                }
                setUpViews(existingNote)
            }
        }


        saveBtn.setOnClickListener {

            val title = titleEdit.text.trim().toString()
            val desc = description.text.trim().toString()

            if (title.isNotEmpty() && desc.isNotEmpty()) {
                lifecycleScope.launch() {
                    putNote(title, desc, existingNote)
                    if (isAddNotes){
                        showToast(this@AddNoteActivity, "Record added successfully")
                    }else {
                        showToast(this@AddNoteActivity, "Record updated successfully")
                    }

                    finish()
                }
            }
        }
    }

    private fun setUpViews(existingNote: Note?) {

        // If editing an existing Note, restore its data to the UI.
        existingNote?.also {
            titleEdit.setText(it.title ?: "")
            description.setText(it.description ?: "")
        }
    }

    private suspend fun putNote(
        noteTitle: String,
        noteDesc: String,
        existingNote: Note?
    ) = withContext(Dispatchers.IO) {
        val note = // Update existing Note Object.
            existingNote?.apply {
                title = noteTitle
                description = noteDesc
            }
                ?: Note(title = noteTitle, description = noteDesc, date = Date())
        ObjectBox.boxStore.boxFor(Note::class.java).put(note)
    }

    private fun checkValidations() {
        if (isTitleValid && isDescriptionValid) {
            setButtonActivatedWithColor(this, saveBtn, R.color.dusk_blue_two)
            saveBtn.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            setButtonDeactivated(this, saveBtn, false, R.color.address_submit_disable_bg)
            saveBtn.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }

    private fun editTextFocusLostListener(
        editText: EditText,
        componentValue: String
    ) {

        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                when (componentValue) {
                    TITLE_NAME -> {
                        if (editText.text.trim().toString().isEmpty()) {
                            titleTextError.text = Constant.TITLE_REQUIRED
                            titleTextError.visibility = View.VISIBLE
                        }
                    }

                    DESCRIPTION_NAME -> {
                        if (editText.text.trim().toString().isEmpty()) {
                            descTextError.text = Constant.DESCRIPTION_REQUIRED
                            descTextError.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun editTextChangeListener(
        editText: EditText,
        componentValue: String
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {

                when (componentValue) {
                    TITLE_NAME -> {
                        isTitleValid = if (cs.trim().toString().isEmpty()) {
                            titleTextError.text = Constant.TITLE_REQUIRED
                            titleTextError.visibility = View.VISIBLE
                            false
                        } else {
                            titleTextError.text = ""
                            titleTextError.visibility = View.INVISIBLE
                            true
                        }
                    }

                    DESCRIPTION_NAME -> {
                        isDescriptionValid = when {
                            cs.trim().toString().isEmpty() -> {
                                descTextError.text = Constant.DESCRIPTION_REQUIRED
                                descTextError.visibility = View.VISIBLE
                                false
                            }

                            else -> {
                                descTextError.text = ""
                                descTextError.visibility = View.INVISIBLE
                                true
                            }
                        }
                    }
                }
                checkValidations()
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            }

            override fun afterTextChanged(arg0: Editable) {
            }
        })
    }

    companion object {
        const val TITLE_NAME = "title_name"
        const val DESCRIPTION_NAME = "description_name"
    }
}