package com.fminb.zxingclock.main.paresenter

import com.fminb.zxingclock.main.view.MainView


class MainPresenterImpl(private val mainView: MainView) : MainPresenter {
    override fun onViewCreated() {
        mainView.setMenuClick()
    }

    override fun onMenuClick() {
        mainView.showPopupMenu()
    }

    override fun onSettingsClick() {
        mainView.toast("Click Setting")
    }

    override fun onAboutClick() {
        mainView.startAboutActivity()
    }
}