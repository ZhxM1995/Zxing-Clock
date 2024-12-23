package com.fminb.zxingclock

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fminb.zxingclock.databinding.ActivityMainBinding
import com.fminb.zxingclock.paresenter.MainPresenter
import com.fminb.zxingclock.paresenter.MainPresenterImpl
import com.fminb.zxingclock.view.MainView

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
}