package com.blueskylct.eread.ui.reading

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
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
        _viewModel.loadChapters()
        val list = _viewModel.chapterListLiveData.value as ArrayList
        binding.chapterRecyclerview.adapter = ChapterListAdapter(list, this)
        binding.chapterRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        _viewModel.setContent(list[0])

        _viewModel.chapterContentLiveData.observe(this){
            binding.vm.clearFormData()
            binding.vm.clearHistory()
            showChapter(it)
        }

        binding.vm.setOnClickListener { toggleToolbars() }
    }

    //显示章节内容
    private fun showChapter(content: String){
        binding.vm.loadDataWithBaseURL(null, content, "text/html", "utf-8", null)
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