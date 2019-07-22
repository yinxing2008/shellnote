package com.cxyzy.note.viewmodels

import com.cxyzy.note.events.RefreshEvent
import com.cxyzy.note.events.SyncEvent
import com.cxyzy.note.network.LoginManager
import com.cxyzy.note.network.response.BaseResp
import com.cxyzy.note.network.response.EmptyResp
import com.cxyzy.note.utils.spUtils.UserSPUtil.delLoginRespInSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.delUserIdInSP
import org.greenrobot.eventbus.EventBus

class UserViewModel : BaseViewModel() {
    fun login(loginId: String,
              password: String,
              onSuccess: (() -> Unit)? = null,
              onFailure: ((resp: BaseResp<EmptyResp>) -> Unit)? = null,
              onFinished: (() -> Unit)? = null) {

        launchOnUITryCatch(
                {
                    LoginManager.loginAsync(loginId, password,
                            {
                                EventBus.getDefault().post(SyncEvent())
                                EventBus.getDefault().post(RefreshEvent())
                                onSuccess?.invoke()
                            }
                            , onFailure)
                },
                { onFailure?.invoke(BaseResp(message = it.message)) },
                { onFinished?.invoke() })
    }

    fun logout() {
        delLoginRespInSP()
        delUserIdInSP()
    }
}