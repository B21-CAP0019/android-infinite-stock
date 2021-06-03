package com.example.infinitestock.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.databinding.ActivitySignInBinding
import com.example.infinitestock.ui.main.MainActivity
import com.example.infinitestock.ui.signup.SignUpActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivitySignInBinding? = null
    val binding get() = _binding!!
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignInBinding.inflate(layoutInflater)
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

        val url = resources.getString(R.string.server) + "/auth/signin"
        val client = AsyncHttpClient()

        val params = RequestParams()
        params.put("email", email)
        params.put("password", password)

        client.post(url, params, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                // success
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                TODO("Not yet implemented")
            }

        })
        val intentToMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intentToMainActivity)
    }

    private fun performLoading() {
        with (binding) {
            if (!isLoading) {
                btnSignin.visibility = View.GONE
            }
        }
    }
}