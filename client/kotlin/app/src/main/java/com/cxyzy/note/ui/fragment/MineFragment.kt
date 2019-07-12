package com.cxyzy.note.ui.fragment

import android.content.Intent
import com.cxyzy.note.R
import com.cxyzy.note.events.BaseEvent
import com.cxyzy.note.events.RefreshEvent
import com.cxyzy.note.network.LoginManager
import com.cxyzy.note.ui.activity.LoginActivity
import com.cxyzy.note.ui.base.BaseFragment
import com.cxyzy.note.utils.setVisibility
import com.cxyzy.note.utils.spUtils.UserSPUtil.getLoginIdFromSP
import com.cxyzy.note.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.getViewModel

class MineFragment : BaseFragment<UserViewModel>() {

    override fun viewModel(): UserViewModel = getViewModel()
    override fun layoutId(): Int = R.layout.fragment_mine

    override fun initListeners() {
        logoutTv.setOnClickListener {
            viewModel().logout()
            refreshUI()
        }
    }

    override fun initViews() {
        refreshUI()
    }

    private fun refreshUI() {
        logoutTv.setVisibility(LoginManager.isLoggedIn())
        if (LoginManager.isLoggedIn()) {
            loginIdTv.text = getLoginIdFromSP()
            loginLayout.setOnClickListener(null)
        } else {
            loginIdTv.text = getString(R.string.login)
            loginLayout.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
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
