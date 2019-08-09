package com.cxyzy.note.viewmodels

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.cxyzy.note.db.bean.Note
import com.cxyzy.note.db.repository.NoteRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : BaseViewModel() {
    fun add(content: String,
            onSuccess: (() -> Unit)? = null,
            onError: ((throwable: Throwable) -> Unit)? = null,
            onFinish: (() -> Unit)? = null) {

        launchOnUITryCatch(
                {
                    noteRepository.add(content)
                    onSuccess?.invoke()
                },
                {
                    onError?.invoke(it)
                    error(it)
                },
                { onFinish?.invoke() },
                true)
    }

    fun update(note: Note) {
        GlobalScope.launch {
            note.isDirty = true
            noteRepository.update(note)
        }
    }

    fun del(id: String) {
        GlobalScope.launch {
            noteRepository.trash(id)
        }
    }

    fun getList(): LiveData<PagedList<Note>> {
        val list = noteRepository.list()
        return list
    }
}