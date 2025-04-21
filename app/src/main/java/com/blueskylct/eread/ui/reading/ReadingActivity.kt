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
import com.blueskylct.eread.utils.EpubUtil

class ReadingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityReadingBinding
    private val _viewModel by lazy {
        ViewModelProvider(this)[ReadingViewModel::class.java]
    }
    val viewModel get() = _viewModel
    private var isToolBarVisible = false
    private var clickedTime: Long = 0

    @SuppressLint("ClickableViewAccessibility", "SetJavaScriptEnabled")
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

        binding.wv.apply {
            //设置WebView
            settings.apply {
                javaScriptEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                allowFileAccessFromFileURLs = true
                allowUniversalAccessFromFileURLs = true
            }
            webViewClient = object: WebViewClient(){
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        if (it.startsWith("file://"))
                            view?.loadUrl(url)
                    }
                    return true
                }
            }
            //为WebView获取系统焦点
            requestFocus()
            requestFocusFromTouch()
            //拦截WebView的触摸事件
            setOnTouchListener {
                    v, event ->
                v.performClick()
                false
            }
        }

        _viewModel.chapterContentLiveData.observe(this){
            val url = "file://${EpubUtil.urlList[viewModel.index]}"
            showChapter(url, it)
        }

        binding.wv.setOnClickListener {
            val clickTime = System.currentTimeMillis()
            if (clickTime > clickedTime + 300){
                clickedTime = clickTime
                toggleToolbars()
            }
        }
    }

    //显示章节内容
    private fun showChapter(url: String, content: String){
        binding.wv.loadDataWithBaseURL(url, content, "text/html", "utf-8", null)
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
            .setDuration(100)
            .start()

        binding.bottomToolbar.visibility = View.VISIBLE

        binding.bottomToolbar.animate()
            .translationY(0f)
            .setDuration(100)
            .start()

    }

    //隐藏工具栏
    private fun hideToolbar(){
        isToolBarVisible = false

        binding.topToolbar.visibility = View.GONE

        binding.topToolbar.animate()
            .translationY(-binding.topToolbar.height.toFloat())
            .setDuration(100)
            .start()

        binding.bottomToolbar.visibility = View.GONE

        binding.bottomToolbar.animate()
            .translationY(binding.bottomToolbar.height.toFloat())
            .setDuration(100)
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}