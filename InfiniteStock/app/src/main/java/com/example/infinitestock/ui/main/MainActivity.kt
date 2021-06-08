package com.example.infinitestock.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.infinitestock.R
import com.example.infinitestock.data.SessionCompat
import com.example.infinitestock.data.entity.Account
import com.example.infinitestock.data.entity.Good
import com.example.infinitestock.data.entity.WarehouseResponse
import com.example.infinitestock.databinding.ActivityMainBinding
import com.example.infinitestock.ui.add.AddGoodsActivity
import com.example.infinitestock.ui.login.LoginActivity
import com.example.infinitestock.ui.stock.HistoryActivity
import com.example.infinitestock.ui.update.UpdateGoodsActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ACCOUNT = "account"
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var account: Account? = null
    private lateinit var vibrate: Vibrator
    private lateinit var goodsAdapter : GoodsAdapter

    private var viewModel: GoodsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goodsAdapter = GoodsAdapter()
        vibrate = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val bundle = intent.extras
        account = bundle?.getParcelable(EXTRA_ACCOUNT)

        with (binding) {
            // list
            recListGoods.layoutManager = LinearLayoutManager(this@MainActivity)
            recListGoods.adapter = goodsAdapter
            goodsAdapter.setOnItemActionListener(object : GoodsAdapter.OnItemActionListener {
                override fun onItemClick(good: Good) {
                    updateGood(good)
                }

                override fun onItemLongClick(good: Good) {
                    deleteGood(good)
                }
            })

            // toolbar
            setSupportActionBar(appbarMain.toolbar)
            supportActionBar?.title = " ${ resources.getString(R.string.app_name) }"
            supportActionBar?.setDisplayUseLogoEnabled(true)
            Glide.with(this@MainActivity)
                .asDrawable()
                .load(R.drawable.logo)
                .override(80, 80)
                .into(object: CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        supportActionBar?.setLogo(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        /* no-op */
                    }
                })

            viewModel = ViewModelProvider(this@MainActivity)[
                    GoodsViewModel::class.java
            ]
            viewModel?.loadGoods()?.observe(this@MainActivity, { applyWarehouseResponse(it) })
            viewModel?.retrieveGoods(this@MainActivity, true)

            btnToAddGoods.setOnClickListener {
                val intentToAddGoodsActivity = Intent(this@MainActivity, AddGoodsActivity::class.java)
                startActivity(intentToAddGoodsActivity)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                refreshGood()
            }
            R.id.menu_history -> {
                val intentToHistory = Intent(this, HistoryActivity::class.java)
                startActivity(intentToHistory)
            }
            R.id.menu_logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("Do you really want to logout from ${ resources.getString(R.string.app_name) }?")
                    .setPositiveButton("Yes, I do") { _, _ ->
                        SessionCompat(this).setAccount(Account("", "", ""))
                        Toast.makeText(this, "You have been logged out.", Toast.LENGTH_SHORT).show()

                        val intentToLogin = Intent(this, LoginActivity::class.java)
                        startActivity(intentToLogin)
                        finishAffinity()
                    }
                    .setNegativeButton("No", null)
                    .setCancelable(false)
                    .create().show()
            }
        }
        return true
    }

    private fun refreshGood() {
        with (binding.customLoading) {
            root.visibility = View.VISIBLE
            animationLoad.visibility = View.VISIBLE
            animationLoad.progress = 0.0F
            animationLoad.playAnimation()

            // empty
            animationEmpty.visibility = View.GONE
            animationEmpty.progress = 0.0F
            //animationEmpty.playAnimation()
            animationEmpty.pauseAnimation()

            // error
            animationError.visibility = View.GONE
            animationError.progress = 0.0F
            //animationError.playAnimation()
            animationError.pauseAnimation()

            tvStatus.text = getString(R.string.loading)
            tvStatus.visibility = View.VISIBLE
        }
        binding.recListGoods.visibility = View.GONE

        GlobalScope.launch(Dispatchers.Main) {
            val deferredGoods = async(Dispatchers.IO) {
                viewModel?.retrieveGoods(this@MainActivity, false)
            }
            val warehouseResponse = deferredGoods.await()
            if (warehouseResponse != null) {
                applyWarehouseResponse(warehouseResponse)
            }
        }
    }

    private fun updateGood(good: Good) {
        val intentToUpdateGoodsActivity = Intent(this, UpdateGoodsActivity::class.java)
        intentToUpdateGoodsActivity.putExtra(UpdateGoodsActivity.EXTRA_GOOD, good)
        startActivity(intentToUpdateGoodsActivity)
    }

    @Suppress("DEPRECATION")
    private fun deleteGood(good: Good) {
        // haptic feedback
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                vibrate.vibrate(
                    VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            else -> vibrate.vibrate(70)
        }

        AlertDialog.Builder(this)
            .setTitle("Are you sure?")
            .setMessage("Are you sure you want to delete '${ good.goodsName }'?")
            .setPositiveButton("Yes, I want to delete it") { _, _ ->
                with (binding.customLoading) {
                    root.visibility = View.VISIBLE
                    animationLoad.visibility = View.VISIBLE
                    animationLoad.progress = 0.0F
                    animationLoad.playAnimation()

                    // empty
                    animationEmpty.visibility = View.GONE
                    animationEmpty.progress = 0.0F
                    //animationEmpty.playAnimation()
                    animationEmpty.pauseAnimation()

                    // error
                    animationError.visibility = View.GONE
                    animationError.progress = 0.0F
                    //animationError.playAnimation()
                    animationError.pauseAnimation()

                    val loadMessage = getString(R.string.delete_good) + " '" + good.goodsName + "'…"

                    tvStatus.text = loadMessage
                    tvStatus.visibility = View.VISIBLE
                }
                binding.recListGoods.visibility = View.GONE

                val url = resources.getString(R.string.server) + "/warehouse/goods/delete/${ good.goodsId }"
                val client = AsyncHttpClient()

                client.delete(url, object : AsyncHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?
                    ) {
                        refreshGood()
                        Toast.makeText(
                            this@MainActivity,
                            "Successfully deleted item: ${ good.goodsName }",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?,
                        error: Throwable?
                    ) {
                        val result = responseBody?.let { String(it) }
                        Log.d("API Error", result!!)
                        with (binding.customLoading) {
                            // empty
                            animationEmpty.visibility = View.GONE
                            animationEmpty.progress = 0.0F
                            //animationEmpty.playAnimation()
                            animationEmpty.pauseAnimation()

                            // error
                            animationError.visibility = View.GONE
                            animationError.progress = 0.0F
                            //animationError.playAnimation()
                            animationError.pauseAnimation()

                            tvStatus.visibility = View.GONE
                            root.visibility = View.GONE
                        }
                        binding.recListGoods.visibility = View.VISIBLE
                        binding.btnToAddGoods.visibility = View.VISIBLE

                        Toast.makeText(
                            this@MainActivity,
                            """
                                Oops, an error was occurred! We will be back as soon as possible.
                                [$statusCode]: ${ error?.localizedMessage.toString() }
                            """.trimIndent(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
            .setNegativeButton("No", null)
            .setCancelable(false)
            .create().show()
    }

    override fun onDestroy() {
        _binding = null
        viewModel = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            super.onBackPressed()
        } else {
            finishAfterTransition()
        }
    }

    private fun applyWarehouseResponse(response: WarehouseResponse) {
        if (response.status == 200) {
            binding.customLoading.animationLoad.visibility = View.GONE

            if (response.goods.size == 0) {
                // When the data is empty
                with (binding.customLoading) {
                    // empty
                    animationEmpty.visibility = View.VISIBLE
                    animationEmpty.progress = 0.0F
                    animationEmpty.playAnimation()
                    //animationEmpty.pauseAnimation()

                    // error
                    animationError.visibility = View.GONE
                    animationError.progress = 0.0F
                    //animationError.playAnimation()
                    animationError.pauseAnimation()

                    tvStatus.text = getString(R.string.empty_data)
                    root.visibility = View.VISIBLE
                }
                binding.recListGoods.visibility = View.GONE
                goodsAdapter.setGoodsValue(response.goods)

                binding.btnToAddGoods.visibility = View.VISIBLE
            } else {
                // When data is not empty
                with (binding.customLoading) {
                    // empty
                    animationEmpty.visibility = View.GONE
                    animationEmpty.progress = 0.0F
                    //animationEmpty.playAnimation()
                    animationEmpty.pauseAnimation()

                    // error
                    animationError.visibility = View.GONE
                    animationError.progress = 0.0F
                    //animationError.playAnimation()
                    animationError.pauseAnimation()

                    tvStatus.visibility = View.GONE
                    root.visibility = View.GONE
                }
                binding.recListGoods.visibility = View.VISIBLE
                goodsAdapter.setGoodsValue(response.goods)

                binding.btnToAddGoods.visibility = View.VISIBLE
            }
        } else {
            // If the response is error unexpectedly
            with (binding.customLoading) {
                // empty
                animationEmpty.visibility = View.GONE
                animationEmpty.progress = 0.0F
                //animationEmpty.playAnimation()
                animationEmpty.pauseAnimation()

                // error
                animationError.visibility = View.VISIBLE
                animationError.progress = 0.0F
                animationError.playAnimation()
                //animationError.pauseAnimation()

                tvStatus.text = getString(R.string.error)
                root.visibility = View.VISIBLE
            }
            Log.e("API Error", response.message!!)
            binding.recListGoods.visibility = View.GONE
            goodsAdapter.setGoodsValue(ArrayList())

            binding.btnToAddGoods.visibility = View.GONE
        }
    }
}