package com.example.infinitestock.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.databinding.ActivitySignInBinding
import com.example.infinitestock.ui.main.MainActivity
import com.example.infinitestock.ui.signup.SignUpActivity
import com.loopj.android.http.AsyncHttpClient

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this@LoginActivity)
            .load(R.drawable.logo)
            .into(binding.logoSignin)

        binding.btnSignin.setOnClickListener {
            onSignInClick()
        }
        binding.textToSignup.setOnClickListener {
            val intentToSignUpActivity = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intentToSignUpActivity)
        }
    }

    private fun onSignInClick() {
        val email = binding.valueSigninEmail.text.toString()
        val password = binding.valueSigninPass.text.toString()

        val endpoint = resources.getString(R.string.server) + "/auth/signin"
        val client = AsyncHttpClient()



        val intentToMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intentToMainActivity)
    }
}