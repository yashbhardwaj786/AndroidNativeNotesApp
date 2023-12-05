package com.epsoftandroid.demo.common

import android.content.Context
import android.content.Intent
import com.epsoftandroid.demo.ui.activity.AddNoteActivity
import com.epsoftandroid.demo.utils.Constant

object ModuleMaster {

    fun navigateToAddNoteActivity(context: Context, isAddNotes: Boolean, noteId: Long = -1) {
        Intent(context, AddNoteActivity::class.java).also {
            it.putExtra("IS_ADD_NOTES", isAddNotes)
            it.putExtra(Constant.EXTRA_NOTE_ID, noteId)
            context.startActivity(it)
        }
    }
}
