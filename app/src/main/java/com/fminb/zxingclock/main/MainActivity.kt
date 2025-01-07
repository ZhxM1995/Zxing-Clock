package com.fminb.zxingclock.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.fminb.zxingclock.R
import com.fminb.zxingclock.about.AboutActivity
import com.fminb.zxingclock.databinding.ActivityMainBinding
import com.fminb.zxingclock.main.paresenter.MainPresenter
import com.fminb.zxingclock.main.paresenter.MainPresenterImpl
import com.fminb.zxingclock.main.view.MainView

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: MainPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = MainPresenterImpl(this)
        presenter.onViewCreated()
    }

    override fun setMenuClick() {
        binding.tvMenu.setOnClickListener {
            presenter.onMenuClick()
        }
    }

    override fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun showPopupMenu() {
        val popupMenu = PopupMenu(this, binding.tvMenu)
        popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    presenter.onSettingsClick()
                    true
                }
                R.id.action_about -> {
                    presenter.onAboutClick()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun startAboutActivity() {
        AboutActivity.start(this)
    }
}