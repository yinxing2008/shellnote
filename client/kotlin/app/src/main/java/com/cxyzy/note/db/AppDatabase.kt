package com.cxyzy.note.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cxyzy.note.USER_DB_NAME_PREFIX
import com.cxyzy.note.db.bean.Note
import com.cxyzy.note.db.converters.Converters
import com.cxyzy.note.db.dao.NoteDao
import com.cxyzy.note.utils.spUtils.UserSPUtil.getUserIdFromSP


@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var currentDatabaseName = ""
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val databaseName = USER_DB_NAME_PREFIX + getUserIdFromSP()
            return if (databaseName == currentDatabaseName) {
                instance ?: synchronized(this) {
                    instance ?: buildDatabase(context, databaseName).also { instance = it }
                }
            } else {
                synchronized(this) {
                    buildDatabase(context, databaseName).also { instance = it }
                }
            }
        }

        private fun buildDatabase(context: Context, databaseName: String): AppDatabase {
            currentDatabaseName = databaseName
            return Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                    .addCallback(object : RoomDatabase.Callback() {
                    })
                    .build()
        }
    }
}
