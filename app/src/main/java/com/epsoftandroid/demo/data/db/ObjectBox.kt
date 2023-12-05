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

//        if (BuildConfig.DEBUG) {
//            var syncAvailable = if (Sync.isAvailable()) "available" else "unavailable"
//            Log.d(EpSoftDemo.TAG,
//                    "Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()}, sync $syncAvailable)")
//            Admin(boxStore).start(context.applicationContext)
//        }

        val notesQuery = boxStore.boxFor(Note::class.java).query()
                .orderDesc(Note_.date)
                .build()

        notesLiveData = ObjectBoxLiveData(notesQuery)

//        if (boxStore.boxFor(Note::class.java).isEmpty) {
//            replaceWithDemoData()
//        }
    }

    private fun replaceWithDemoData() {
        val note1 = writeNote("", "")

        boxStore.boxFor(Note::class.java).put(note1)
    }

    private fun writeNote(textTitle: String, textDescription: String): Note {
        return Note(title = textTitle, description = textDescription,  date = Date())
    }

    fun copyAndGzipDatabaseFileTo(target: File, context: Context): Boolean {
        if (BoxStore.isDatabaseOpen(context, null)) {
            Log.e(EpSoftDemo.TAG, "Database file is still in use, can not copy.")
            return false
        }

        val dbName = BoxStoreBuilder.DEFAULT_NAME
        File(context.filesDir, "objectbox/$dbName/epsoft.mdb").inputStream().use { input ->
            target.parentFile?.mkdirs()
            GZIPOutputStream(target.outputStream()).use { output ->
                input.copyTo(output, DEFAULT_BUFFER_SIZE)
            }
        }
        return true
    }

}