package com.fminb.zxingclock.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.fminb.zxingclock.R
import com.fminb.zxingclock.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    companion object {
        const val TAG = "AboutActivity"

        fun start(context: Context) {
            val intent = Intent(context, AboutActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 设置富文本内容
        binding.tvContent.text = Html.fromHtml(getString(R.string.about_content), Html.FROM_HTML_MODE_LEGACY)
        binding.tvContent.movementMethod = LinkMovementMethod.getInstance()
    }
}