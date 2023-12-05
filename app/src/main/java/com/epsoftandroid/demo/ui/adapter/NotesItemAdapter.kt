package com.epsoftandroid.demo.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.epsoftandroid.demo.R
import com.epsoftandroid.demo.data.model.Note
import com.epsoftandroid.demo.databinding.LayoutNotesItemBinding
import com.epsoftandroid.demo.ui.viewmodel.MainViewModel
import java.text.DateFormat

class NotesItemAdapter(
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<NotesItemAdapter.NotesItemViewHolder>()  {
    private val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
    private val dataset: MutableList<Note> = mutableListOf()
    var context: Context ? = null

    fun setNotes(notes: List<Note>) {
        dataset.apply {
            clear()
            addAll(notes)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesItemViewHolder {
        val binding: LayoutNotesItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_notes_item, parent, false)
        context = parent.context
        return NotesItemViewHolder(binding.root, binding)
    }

    override fun getItemCount() = dataset.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotesItemViewHolder, position: Int) {
        val notesItemData = dataset[position]
        holder.binding.viewModel = mainViewModel
        holder.binding.data = notesItemData

        holder.binding.rootLayout.setOnLongClickListener {

            mainViewModel.removeDataFromList(notesItemData.id ?: -1)
            return@setOnLongClickListener true
        }
    }

    class NotesItemViewHolder(itemView: View, var binding: LayoutNotesItemBinding) :
        RecyclerView.ViewHolder(itemView)
}