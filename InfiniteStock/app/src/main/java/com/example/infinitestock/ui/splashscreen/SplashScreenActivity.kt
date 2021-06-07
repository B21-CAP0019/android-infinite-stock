package com.example.infinitestock.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.data.entity.Account
import com.example.infinitestock.databinding.ActivitySplashScreenBinding
import com.example.infinitestock.ui.login.LoginActivity
import com.example.infinitestock.ui.main.MainActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

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
            binding.animationLoading.visibility = View.VISIBLE
        }, 2000)

        Handler(Looper.getMainLooper()).postDelayed({
            val account = SessionCompat(this).getAccount()

            if (account.publicId.equals("") || account.publicId == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            } else {
                // in this block, put a checkup algorithm for measure that public id is
                // available in the server.
                val url = resources.getString(R.string.server) + "/user/search"
                val client = AsyncHttpClient()

                val params = RequestParams()
                params.put("public_id", account.publicId)

                client.get(url, params, object: AsyncHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray
                    ) {
                        if (statusCode == 200) {
                            val result = String(responseBody)
                            Log.d("API", result)
                            val response = JSONObject(result)
                            val accountData = response.getJSONObject("data")

                            val getCount = accountData.getInt("found")
                            if (getCount > 0) {
                                val accountResponse = Account(
                                    fullName = accountData.getString("full_name"),
                                    publicId = accountData.getString("public_id"),
                                    shopName = accountData.getString("shop_name")
                                )

                                Toast.makeText(
                                    this@SplashScreenActivity,
                                    "Welcome to ${ resources.getString(R.string.app_name) }!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intentToMain = Intent(this@SplashScreenActivity, MainActivity::class.java)
                                intentToMain.putExtra(MainActivity.EXTRA_ACCOUNT, accountResponse)
                                startActivity(intentToMain)
                                finishAffinity()
                            } else {
                                SessionCompat(this@SplashScreenActivity).setAccount(Account(
                                    null,
                                    null,
                                    null
                                ))

                                Toast.makeText(
                                    this@SplashScreenActivity,
                                    "Session expired, please login again.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intentToLogin = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                                startActivity(intentToLogin)
                                finishAffinity()
                            }
                        } else {
                            Toast.makeText(
                                this@SplashScreenActivity,
                                "Error logging in with code $statusCode. Please relaunch application again, or try to login.",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intentToLogin = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                            startActivity(intentToLogin)
                            finishAffinity()
                        }
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?,
                        error: Throwable?
                    ) {
                        Toast.makeText(
                            this@SplashScreenActivity,
                            "Error logging in with code $statusCode. Please relaunch application again, or try to login.",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intentToLogin = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                        startActivity(intentToLogin)
                        finishAffinity()
                    }
                })
            }
        }, 3000)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}