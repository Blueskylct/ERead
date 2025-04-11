package com.blueskylct.eread.ui.reading

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.databinding.ActivityReadingBinding
import com.blueskylct.eread.ui.adapter.ChapterListAdapter
import com.blueskylct.eread.utils.EpubUtil
import nl.siegmann.epublib.domain.Book

class ReadingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityReadingBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[ReadingViewModel::class.java]
    }

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
        val adapter = ChapterListAdapter(viewModel.chapterListLiveData.value as ArrayList)
        binding.chapterRecyclerview.adapter = adapter
        binding.chapterRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val list = EpubUtil.getChapter(MyApplication.getInstance().getBook())
        Log.d("list", list.size.toString() + list.toString() + "\n${list[0]}")
        //viewModel.loadChapter()
        showChapter()

        viewModel.chapterListLiveData.observe(this){
            adapter.updateList(it as ArrayList)
        }
    }

    private fun showChapter(){
        val contents = MyApplication.getInstance().getBook().contents
        val string = StringBuilder()
        for (content in contents) {
            string.append(content.inputStream.readBytes().toString(Charsets.UTF_8))
            content.close()
        }
        binding.vm.loadDataWithBaseURL(null, string.toString(), "text/html", "UTF-8", null)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}