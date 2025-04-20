package com.blueskylct.eread.ui.reading

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueskylct.eread.databinding.ActivityReadingBinding
import com.blueskylct.eread.ui.adapter.ChapterListAdapter

class ReadingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityReadingBinding
    private val _viewModel by lazy {
        ViewModelProvider(this)[ReadingViewModel::class.java]
    }
    val viewModel get() = _viewModel
    private var isToolBarVisible = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //加载章节数据
        _viewModel.loadChapters()
        //加载章节目录
        val list = _viewModel.chapterListLiveData.value as ArrayList
        binding.chapterRecyclerview.adapter = ChapterListAdapter(list, this)
        binding.chapterRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //设置章节目录的分割线
        val divider = DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL)
        binding.chapterRecyclerview.addItemDecoration(divider)
        //初始化WebView内容
        _viewModel.setContent(list[0])
        //为WebView获取系统焦点
        binding.wv.requestFocus()
        binding.wv.requestFocusFromTouch()
        binding.wv.setOnTouchListener {
            v, event ->
            v.performClick()
            false
        }
        Log.d("WebView Focus", binding.wv.hasFocus().toString())
        binding.wv.webViewClient = object: WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.wv.requestFocus()
                binding.wv.requestFocusFromTouch()
            }
        }

        _viewModel.chapterContentLiveData.observe(this){
            showChapter(it)
        }

        binding.wv.setOnClickListener {
            toggleToolbars()
            Log.d("WebView CLicked", "true")
        }
    }

    //显示章节内容
    private fun showChapter(content: String){
        binding.wv.loadDataWithBaseURL(null, content, "text/html", "utf-8", null)
    }

    //工具栏显示切换
    private fun toggleToolbars(){
        if (isToolBarVisible){
            hideToolbar()
        }
        else{
           showToolbar()
        }
    }

    //显示工具栏
    private fun showToolbar(){
        isToolBarVisible = true

        binding.topToolbar.visibility = View.VISIBLE

        binding.topToolbar.animate()
            .translationY(0f)
            .setDuration(300)
            .start()

        binding.bottomToolbar.visibility = View.VISIBLE

        binding.bottomToolbar.animate()
            .translationY(0f)
            .setDuration(300)
            .start()

    }

    //隐藏工具栏
    private fun hideToolbar(){
        isToolBarVisible = false

        binding.topToolbar.visibility = View.GONE

        binding.topToolbar.animate()
            .translationY(-binding.topToolbar.height.toFloat())
            .setDuration(300)
            .start()

        binding.bottomToolbar.visibility = View.GONE

        binding.bottomToolbar.animate()
            .translationY(binding.bottomToolbar.height.toFloat())
            .setDuration(300)
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}