package com.example.infinitestock.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.databinding.ActivitySplashScreenBinding
import com.example.infinitestock.ui.login.LoginActivity
import com.example.infinitestock.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.logo)
            .into(binding.logoSplash)

        Handler(Looper.getMainLooper()).postDelayed({
            val account = SessionCompat(this).getAccount()

            if (account.publicId.equals("") || account.publicId == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                // in this block, put a checkup algorithm for measure that public id is
                // available in the server.
                Toast.makeText(
                    this,
                    "Welcome to ${ resources.getString(R.string.app_name) }!",
                    Toast.LENGTH_SHORT
                ).show()

                val intentToMain = Intent(this, MainActivity::class.java)
                intentToMain.putExtra(MainActivity.EXTRA_ACCOUNT, account)
                startActivity(intentToMain)
                finishAffinity()
            }
        }, 3000)
    }
}