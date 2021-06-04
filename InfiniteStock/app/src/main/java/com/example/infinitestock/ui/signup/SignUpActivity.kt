package com.example.infinitestock.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.databinding.ActivitySignUpBinding
import com.example.infinitestock.ui.main.MainActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this@SignUpActivity)
            .load(R.drawable.logo)
            .into(binding.logoSignup)

        binding.btnSignup.setOnClickListener {
            val intentToLoginActivity = Intent(this@SignUpActivity, MainActivity::class.java)
            startActivity(intentToLoginActivity)
        }
    }
}