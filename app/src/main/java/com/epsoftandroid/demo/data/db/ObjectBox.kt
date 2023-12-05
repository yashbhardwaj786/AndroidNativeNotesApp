package com.epsoftandroid.demo.data.db

import android.content.Context
import android.util.Log
import com.epsoftandroid.demo.EpSoftDemo
import com.epsoftandroid.demo.data.model.MyObjectBox
import com.epsoftandroid.demo.data.model.Note
import com.epsoftandroid.demo.data.model.Note_
import io.objectbox.BoxStore
import io.objectbox.BoxStoreBuilder
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.exception.DbException
import io.objectbox.exception.FileCorruptException
import java.io.File
import java.util.*
import java.util.zip.GZIPOutputStream

object ObjectBox {

    lateinit var boxStore: BoxStore
        private set

    var dbExceptionMessage: String? = null
        private set

    lateinit var notesLiveData: ObjectBoxLiveData<Note>
        private set

    fun init(context: Context) {
        boxStore = try {
            MyObjectBox.builder()
                    .androidContext(context.applicationContext)
                    .build()
        } catch (e: DbException) {
            if (e.javaClass.equals(DbException::class.java) || e is FileCorruptException) {
                dbExceptionMessage = e.toString()
                return
            } else {
                throw e
            }
        }

        val notesQuery = boxStore.boxFor(Note::class.java).query()
                .orderDesc(Note_.date)
                .build()

        notesLiveData = ObjectBoxLiveData(notesQuery)
    }

}