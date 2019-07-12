package com.cxyzy.note.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cxyzy.note.viewmodels.BaseViewModel
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected abstract fun viewModel(): VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, layoutId(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVM()
        prepareBeforeInitView()
        initViews()
        initListeners()
        startObserve()
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * 布局文件id
     */
    abstract fun layoutId(): Int

    open fun prepareBeforeInitView() {}
    open fun initViews() {}
    open fun initListeners() {}
    open fun startObserve() {}

    private fun observeVM() {
        lifecycle.addObserver(viewModel())
    }

    override fun onDestroy() {
        viewModel().let {
            lifecycle.removeObserver(it)
        }
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    protected open fun isRegisterEventBus(): Boolean {
        return false
    }
}