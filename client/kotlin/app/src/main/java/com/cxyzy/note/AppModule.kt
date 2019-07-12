package com.cxyzy.note

import com.cxyzy.note.db.AppDatabase
import com.cxyzy.note.db.repository.NoteRepository
import com.cxyzy.note.network.HttpRepository
import com.cxyzy.note.viewmodels.NoteViewModel
import com.cxyzy.note.viewmodels.UserViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory {
        AppDatabase.getInstance(androidApplication())
    }
    factory { get<AppDatabase>().noteDao() }
    single { NoteRepository() }
    viewModel { NoteViewModel(get()) }

    single { HttpRepository }
    viewModel { UserViewModel() }
}
