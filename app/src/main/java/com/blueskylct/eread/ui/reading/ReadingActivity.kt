package com.blueskylct.eread.ui.reading

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueskylct.eread.MyApplication
import com.blueskylct.eread.databinding.ActivityReadingBinding
import com.blueskylct.eread.ui.adapter.ChapterListAdapter
import java.util.ArrayList

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
        //val book = MyApplication.getInstance().getBook()
        //val guide = book.guide
        binding.chapterRecyclerview.adapter = ChapterListAdapter(viewModel.chapterListLiveData.value as  ArrayList)
        binding.chapterRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        showChapter()
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