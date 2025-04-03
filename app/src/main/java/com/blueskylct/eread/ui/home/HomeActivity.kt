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
    private val _viewModel by lazy {ViewModelProvider(this)[HomeViewModel::class.java]}
        val viewModel get() = _viewModel
    private val mLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri -> uri?.let { pickEpubBook(uri) }
    }
    private lateinit var adapter: BookListAdapter

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
        _viewModel.loadBook()
        adapter = BookListAdapter(_viewModel.bookListLiveData.value as ArrayList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this, VERTICAL, false)

        binding.ftb.setOnClickListener {
            if (PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, binding.ftb.id % 65536))
                mLauncher.launch("application/epub+zip")
        }

        _viewModel.bookListLiveData.observe(this) {
            adapter.updateData(it as ArrayList)
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
            else{
                Toast.makeText(this, "需开启权限才能读取文件", Toast.LENGTH_LONG).show()
             }
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