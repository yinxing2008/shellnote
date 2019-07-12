package com.cxyzy.note.ui.activity

import com.cxyzy.note.ExtraKey.KEY_NOTE
import com.cxyzy.note.R
import com.cxyzy.note.db.bean.Note
import com.cxyzy.note.events.SyncEvent
import com.cxyzy.note.ui.base.BaseActivity
import com.cxyzy.note.utils.KeyBoardUtil.showSoftInput
import com.cxyzy.note.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.activity_edit_note.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.viewmodel.ext.android.getViewModel

/**
 * Base class for add_note_float and edit note
 */
abstract class BaseNoteActivity : BaseActivity<NoteViewModel>() {

    override fun viewModel(): NoteViewModel = getViewModel()

    protected var note: Note? = null

    override fun layoutId(): Int = R.layout.activity_edit_note

    override fun prepareBeforeInitView() {
        note = intent?.getParcelableExtra(KEY_NOTE)
    }

    override fun initViews() {
        listNoteToolbar.inflateMenu(R.menu.edit_note_menu)
        listNoteToolbar.setNavigationOnClickListener {
            saveNoteAndFinish()
        }
        initTitle()
        initContent()
        initMenu()
        showSoftInput(this, contentET)
    }

    override fun initListeners() {
        listNoteToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.submitNoteMenuItem -> saveNoteAndFinish()
                R.id.delNoteMenuItem -> trashNote()
            }
            false
        }
    }

    private fun saveNoteAndFinish() {
        if (contentET.text.toString().isNotBlank()) {
            saveNote(contentET.text.toString())
        }
        finish()
    }

    private fun trashNote() {
        note?.let { viewModel().del(it.id) }
        sendSyncEvent()
        finish()
    }

    override fun onBackPressed() {
        saveNoteAndFinish()
        super.onBackPressed()
    }

    protected fun sendSyncEvent() = EventBus.getDefault().post(SyncEvent())

    abstract fun initTitle()
    open fun initContent() {}
    abstract fun initMenu()
    abstract fun saveNote(content: String)

}
