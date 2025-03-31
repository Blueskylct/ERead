package com.blueskylct.eread.ui.home

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.*
import com.blueskylct.eread.databinding.ActivityHomeBinding
import com.blueskylct.eread.ui.adapter.BookListAdapter
import com.blueskylct.eread.utils.EpubUtil
import com.blueskylct.eread.utils.PermissionUtil

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private val viewModel by lazy {ViewModelProvider(this)[HomeViewModel::class.java]}
    private val mLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri -> uri?.let { pickEpubBook(uri) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.recyclerview.adapter = BookListAdapter(viewModel.bookList!!)
        binding.recyclerview.layoutManager = LinearLayoutManager(this, VERTICAL, false)

        binding.ftb.setOnClickListener {
            if (PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, binding.ftb.id % 65536))
                mLauncher.launch("application/epub+zip")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == binding.ftb.id % 65536){
             if (PermissionUtil.checkGrant(grantResults)) {
                 mLauncher.launch("application/epub+zip")
             }
/*            else{
                 PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, binding.ftb.id % 65536)
             }*/
         }
    }

    private fun pickEpubBook(uri: Uri){
        try {
            EpubUtil.loadEpubFromUri(this, uri)
        }catch (e: Exception){
            Toast.makeText(this, "读取失败", Toast.LENGTH_LONG).show()
        }
    }
}