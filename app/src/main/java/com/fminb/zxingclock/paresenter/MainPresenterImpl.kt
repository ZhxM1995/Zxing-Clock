package com.fminb.zxingclock.paresenter

import com.fminb.zxingclock.view.MainView


class MainPresenterImpl(private val mainView: MainView) : MainPresenter {
    override fun onViewCreated() {
        mainView.setMenuClick()
    }

    override fun onMenuClick() {
        mainView.toast("Menu Clicked")
    }
}