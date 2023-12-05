package com.epsoftandroid.demo.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import java.util.*

@Entity
data class Note(
    @Id
    var id: Long? = 0,
    var title: String? = null,
    var description: String? = null,
    @Index
    var date: Date? = null
)