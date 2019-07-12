package com.cxyzy.note.db.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Note(@PrimaryKey var id: String,
                var title: String? = null,
                var content: String? = null,
                var isTrash: Boolean = false,
                var isDeleted: Boolean = false,
                var isDirty: Boolean = true,
                var usn: Long? = null,
                var createTime: Long = Date().time,
                var updateTime: Long = Date().time) : Parcelable
