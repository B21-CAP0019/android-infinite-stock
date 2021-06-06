package com.example.infinitestock.ui.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.example.infinitestock.data.entity.WarehouseResponse
import com.example.infinitestock.databinding.ActivityMainBinding
import com.example.infinitestock.ui.add.AddGoodsActivity
import com.example.infinitestock.ui.login.LoginActivity
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
    private lateinit var goodsAdapter : GoodsAdapter

    private lateinit var viewModel: GoodsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goodsAdapter = GoodsAdapter()

        val bundle = intent.extras
        account = bundle?.getParcelable(EXTRA_ACCOUNT)

        with (binding) {
            // list
            recListGoods.layoutManager = LinearLayoutManager(this@MainActivity)
            recListGoods.adapter = goodsAdapter

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
            viewModel.loadGoods().observe(this@MainActivity, { applyWarehouseResponse(it) })
            viewModel.retrieveGoods(this@MainActivity, account, true)

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
                }

                GlobalScope.launch(Dispatchers.Main) {
                    val deferredGoods = async(Dispatchers.IO) {
                        viewModel.retrieveGoods(this@MainActivity, account, false)
                    }
                    val warehouseResponse = deferredGoods.await()
                    applyWarehouseResponse(warehouseResponse)
                }
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