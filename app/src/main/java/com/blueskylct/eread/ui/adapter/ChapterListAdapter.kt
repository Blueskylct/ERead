package com.blueskylct.eread.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueskylct.eread.databinding.ItemChapterBinding
import com.blueskylct.eread.ui.reading.ReadingActivity

class ChapterListAdapter(private val chapters: ArrayList<String>, private val activity: ReadingActivity):
    RecyclerView.Adapter<ChapterListAdapter.ChapterListViewHolder>() {

    inner class ChapterListViewHolder(binding: ItemChapterBinding): RecyclerView.ViewHolder(binding.root){
        val chapter = binding.chapter
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChapterListViewHolder {
        val binding = ItemChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChapterListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChapterListViewHolder,
        position: Int
    ) {
        val chapter = chapters[position]
        holder.chapter.text = position.toString()
        holder.itemView.setOnClickListener {
            activity.viewModel.apply {
                setContent(chapter)
                setIndex(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return chapters.size
    }
}