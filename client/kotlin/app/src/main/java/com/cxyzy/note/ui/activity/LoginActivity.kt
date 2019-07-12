package com.cxyzy.note.ui.activity

import com.cxyzy.note.R
import com.cxyzy.note.ui.base.BaseActivity
import com.cxyzy.note.utils.KeyBoardUtil
import com.cxyzy.note.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.getViewModel

class LoginActivity : BaseActivity<UserViewModel>() {

    override fun viewModel(): UserViewModel = getViewModel()
    override fun layoutId(): Int = R.layout.activity_login

    override fun initListeners() {
        loginTv.setOnClickListener { login(loginIdEt.text.toString(), passwordEt.text.toString()) }
    }

    private fun login(loginId: String, password: String) {
        KeyBoardUtil.hideSoftInput(this)
        viewModel().login(loginId, password,
                onSuccess = { finish() },
                onFailure = {
                    toast(it.message ?: it.code.toString())
                })
    }
}
