package com.cxyzy.note.ui.activity

import com.cxyzy.note.R
import com.cxyzy.note.db.bean.Note
import kotlinx.android.synthetic.main.activity_edit_note.*

class EditNoteActivity : BaseNoteActivity() {

    override fun initTitle() {
        listNoteToolbar.title = getString(R.string.edit_note)
    }

    override fun initContent() {
        contentET.setText(note?.content)
    }

    override fun initMenu() {
        listNoteToolbar.menu.findItem(R.id.delNoteMenuItem).isVisible = true
    }

    override fun saveNote(content: String) {
        note?.let {
            if (isModified(it, content)) {
                it.content = content
                viewModel().update(it)
                sendSyncEvent()
            }
        }
    }

    private fun isModified(note: Note, content: String) =
            !note.content.equals(content)
}
