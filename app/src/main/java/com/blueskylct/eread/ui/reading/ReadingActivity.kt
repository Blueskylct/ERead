package com.blueskylct.eread.ui.reading

import android.os.Bundle
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
    private val viewModel by lazy {
        ViewModelProvider(this)[ReadingViewModel::class.java]
    }
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
        val list = viewModel.chapterListLiveData.value as ArrayList
        val adapter = ChapterListAdapter(list)
        binding.chapterRecyclerview.adapter = adapter
        binding.chapterRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewModel.loadChapter()

        viewModel.chapterListLiveData.observe(this){
            adapter.updateList(it as ArrayList)
            showChapter(list[4])
        }

        binding.vm.setOnClickListener { toggleToolbars() }
    }

    //显示章节内容
    private fun showChapter(content: String){
        binding.vm.loadData(content, "text/html", "UTF-8")
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

        binding.topToolbar.animate()
            .translationY(0f)
            .setDuration(300)
            .start()

        binding.bottomToolbar.animate()
            .translationY(0f)
            .setDuration(300)
            .start()

    }

    //隐藏工具栏
    private fun hideToolbar(){
        isToolBarVisible = false

        binding.topToolbar.animate()
            .translationY(-binding.topToolbar.height.toFloat())
            .setDuration(300)
            .start()

        binding.bottomToolbar.animate()
            .translationY(binding.bottomToolbar.height.toFloat())
            .setDuration(300)
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}