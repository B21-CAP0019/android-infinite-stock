package com.example.infinitestock.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.infinitestock.databinding.ActivitySignUpBinding
import com.example.infinitestock.ui.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            val intentToLoginActivity = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intentToLoginActivity)
        }
    }
}