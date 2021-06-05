package com.example.infinitestock.ui.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ACCOUNT = "account"
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var account: Account? = null
    private lateinit var goodsAdapter : GoodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goodsAdapter = GoodsAdapter()

        val bundle = intent.extras
        account = bundle?.getParcelable(EXTRA_ACCOUNT)

        with (binding) {
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

            val viewModel = ViewModelProvider(this@MainActivity)[
                    GoodsViewModel::class.java
            ]
            viewModel.loadGoods().observe(this@MainActivity, { applyWarehouseResponse(it) })
            viewModel.retrieveGoods(this@MainActivity, account)

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
            R.id.menu_logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("Do you really want to logout from ${ resources.getString(R.string.app_name) }?")
                    .setPositiveButton("Yes, I do") { _, _ ->
                        SessionCompat(this).setAccount(Account(null, null, null))
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
            if (response.totalData == 0) {
                // TODO: When the data is empty
            } else {
                // TODO: When data is not empty
            }
        } else {
            //TODO: If the response is error unexpectedly
        }
    }
}