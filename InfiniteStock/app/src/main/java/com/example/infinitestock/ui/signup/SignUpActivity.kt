package com.example.infinitestock.ui.signup

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.databinding.ActivitySignUpBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {

    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this@SignUpActivity)
            .load(R.drawable.logo)
            .into(binding.logoSignup)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.valueConfirmationPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager =
                    getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    binding.valueConfirmationPassword.windowToken, 0
                )

                switchLoading()
                onSignInActionPerformed()
            }
            return@setOnEditorActionListener true
        }

        binding.btnSignup.setOnClickListener {
            switchLoading()
            onSignInActionPerformed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return true
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun onSignInActionPerformed() {
        with (binding) {
            val email = valueSignupEmail.text.toString()
            val password = valueSignupPass.text.toString()
            val confirmPassword = valueConfirmationPassword.text.toString()

            if (confirmPassword == "" || password == "" || email == "") {
                Toast.makeText(
                    this@SignUpActivity,
                    "You must fill all the registration form above!",
                    Toast.LENGTH_SHORT
                ).show()
                switchLoading()
            } else {
                if (password == confirmPassword) {
                    val url = resources.getString(R.string.server) + "/auth/signup"
                    val client = AsyncHttpClient()

                    val params = RequestParams()
                    params.put("email", email)
                    params.put("password", password)

                    client.post(url, params, object : AsyncHttpResponseHandler() {
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            responseBody: ByteArray
                        ) {
                            if (statusCode == 201) {
                                val result = String(responseBody)
                                val response = JSONObject(result)

                                val message = response.getString("message")
                                val status = response.getInt("status")

                                Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT)
                                    .show()

                                if (status == 1) {
                                    finish()
                                }
                            }
                            switchLoading()
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            responseBody: ByteArray,
                            error: Throwable?
                        ) {
                            switchLoading()

                            val result = String(responseBody)
                            Log.d("API Error", result)
                            val response = JSONObject(result)

                            if (statusCode == 403) {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    response.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "[${ statusCode }]: ${ response.getString("message") } (${ error?.message.toString() })",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    })
                } else {
                    switchLoading()
                    Toast.makeText(
                        this@SignUpActivity,
                        "Password can't be confirmed. Confirmed password aren't same!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun switchLoading() {
        with (binding) {
            if (!isLoading) {
                btnSignup.visibility = View.GONE
                loading.visibility = View.VISIBLE
            } else {
                btnSignup.visibility = View.VISIBLE
                loading.visibility = View.GONE
            }

            isLoading = !isLoading
        }
    }
}