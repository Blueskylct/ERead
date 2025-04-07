package com.blueskylct.eread.ui.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.blueskylct.eread.R
import com.blueskylct.eread.databinding.ActivityHomeBinding
import com.blueskylct.eread.ui.adapter.BookListAdapter
import com.blueskylct.eread.ui.reading.ReadingActivity
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
        adapter = BookListAdapter(_viewModel.bookListLiveData.value as ArrayList, this)
        binding.recyclerview.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerview.adapter = adapter

        binding.ftb.setOnClickListener {
            if (PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, binding.ftb.id % 65536))
                mLauncher.launch("application/epub+zip")
        }

        _viewModel.bookListLiveData.observe(this) {
            adapter.updateData(it as ArrayList)
        }
    }

    //重写对请求权限的结果处理
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

    //对选择的epub文件进行处理
    private fun pickEpubBook(uri: Uri){
        if (EpubUtil.loadEpubFromUri(this, uri))
            startActivity(Intent(this, ReadingActivity::class.java))
        else
            Log.d("load", "加载epub失败")
    }

    //重写溢出菜单布局
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_overflow, menu)
        return true
    }

    //处理菜单选项
    override fun onOptionsMenuClosed(menu: Menu?) {
        super.onOptionsMenuClosed(menu)
    }
}