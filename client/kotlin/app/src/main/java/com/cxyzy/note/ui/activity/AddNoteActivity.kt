package com.cxyzy.note.ui.activity

import com.cxyzy.note.R
import kotlinx.android.synthetic.main.activity_edit_note.*

class AddNoteActivity : BaseNoteActivity() {
    override fun initTitle() {
        listNoteToolbar.title = getString(R.string.add_note)
    }

    override fun initMenu() {
        listNoteToolbar.menu.findItem(R.id.delNoteMenuItem).isVisible = false
    }

    override fun saveNote(content: String) {
        viewModel().add(content)
        sendSyncEvent()
    }

}
