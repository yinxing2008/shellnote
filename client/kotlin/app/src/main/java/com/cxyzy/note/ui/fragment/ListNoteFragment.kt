package com.cxyzy.note.ui.fragment

import android.content.Intent
import androidx.lifecycle.Observer
import com.cxyzy.note.ExtraKey.KEY_NOTE
import com.cxyzy.note.R
import com.cxyzy.note.events.BaseEvent
import com.cxyzy.note.events.RefreshEvent
import com.cxyzy.note.events.SyncEvent
import com.cxyzy.note.network.LoginManager
import com.cxyzy.note.ui.activity.AddNoteActivity
import com.cxyzy.note.ui.activity.EditNoteActivity
import com.cxyzy.note.ui.adapter.NoteAdapter
import com.cxyzy.note.ui.base.BaseFragment
import com.cxyzy.note.utils.setVisibility
import com.cxyzy.note.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.getViewModel

class ListNoteFragment : BaseFragment<NoteViewModel>() {

    override fun viewModel(): NoteViewModel = getViewModel()
    override fun layoutId(): Int = R.layout.fragment_note

    override fun initListeners() {
        addNoteIv.setOnClickListener {
            startActivity(Intent(activity, AddNoteActivity().javaClass))
        }
        //设置下拉刷新转圈的颜色
//        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            EventBus.getDefault().post(SyncEvent())
        }
    }

    override fun initViews() {
        refreshUIForLoggedIn()
    }

    private fun refreshUI() {
        if (LoginManager.isLoggedIn()) {
            refreshUIForLoggedIn()
        } else {
            refreshUIForBeforeLogin()
        }
    }

    private fun refreshUIForLoggedIn() {
        notesRv.setVisibility(true)
        beforeLoginView.setVisibility(false)
        val adapter = NoteAdapter()
        adapter.onItemClick = { note ->
            val intent = Intent(activity, EditNoteActivity().javaClass)
            intent.putExtra(KEY_NOTE, note)
            startActivity(intent)
        }
        notesRv.adapter = adapter
        activity?.let { viewModel().getList().observe(it, Observer(adapter::submitList)) }
    }

    private fun refreshUIForBeforeLogin() {
        notesRv.setVisibility(false)
        beforeLoginView.setVisibility(true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BaseEvent) {
        if (event is RefreshEvent) {
            refreshUI()
        }
    }

    override fun isRegisterEventBus(): Boolean {
        return true
    }


}
