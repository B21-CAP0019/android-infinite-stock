package com.example.infinitestock.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.data.entity.Account
import com.example.infinitestock.databinding.ActivitySignInBinding
import com.example.infinitestock.ui.main.MainActivity
import com.example.infinitestock.ui.signup.SignUpActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivitySignInBinding? = null
    private val binding get() = _binding!!
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this@LoginActivity)
            .load(R.drawable.logo)
            .into(binding.logoSignin)

        binding.valueSigninPass.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.valueSigninPass.windowToken, 0)

                onSignInClick()
            }
            return@setOnEditorActionListener true
        }

        binding.btnSignin.setOnClickListener {
            onSignInClick()
        }

        binding.textToSignup.setOnClickListener {
            val intentToSignUpActivity = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intentToSignUpActivity)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun onSignInClick() {
        switchLoading()
        val email = binding.valueSigninEmail.text.toString()
        val password = binding.valueSigninPass.text.toString()

        val url = resources.getString(R.string.server) + "/auth/signin"
        val client = AsyncHttpClient()

        val params = RequestParams()
        params.put("email", email)
        params.put("password", password)

        client.get(url, params, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                // success
                val result = String(responseBody)
                try {
                    val response = JSONObject(result)

                    val accountData = response.getJSONObject("data")
                    val account = Account(
                        publicId = accountData.getString("public_id"),
                        fullName = accountData.getString("full_name"),
                        shopName = accountData.getString("shop_name")
                    )

                    SessionCompat(this@LoginActivity).setAccount(account)

                    Toast.makeText(
                        this@LoginActivity,
                        "Welcome to ${ resources.getString(R.string.app_name) }!",
                        Toast.LENGTH_SHORT
                    ).show()

                    switchLoading()

                    val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
                    intentToMain.putExtra(MainActivity.EXTRA_ACCOUNT, account)
                    startActivity(intentToMain)
                    finishAffinity()
                } catch (e: JSONException) {
                    switchLoading()

                    Toast.makeText(
                        this@LoginActivity,
                        "[${ statusCode }]: ${ e.message.toString() }",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray,
                error: Throwable?
            ) {
                switchLoading()

                val result = String(responseBody)
                val response = JSONObject(result)

                Toast.makeText(
                    this@LoginActivity,
                    "[${ statusCode }]: ${ response.getString("message") } (${ error?.message.toString() })",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun switchLoading() {
        with (binding) {
            if (!isLoading) {
                btnSignin.visibility = View.GONE
                loading.visibility = View.VISIBLE
                textToSignup.visibility = View.GONE
            } else {
                btnSignin.visibility = View.VISIBLE
                loading.visibility = View.GONE
                textToSignup.visibility = View.VISIBLE
            }

            isLoading = !isLoading
        }
    }
}