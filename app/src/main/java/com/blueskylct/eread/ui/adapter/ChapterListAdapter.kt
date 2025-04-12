package com.blueskylct.eread.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueskylct.eread.databinding.ItemChapterBinding

class ChapterListAdapter(private val chapters: ArrayList<String>): RecyclerView.Adapter<ChapterListAdapter.ChapterListViewHolder>() {

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
        holder.chapter.text = chapter.length.toString()
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<String>){
        chapters.clear()
        chapters.addAll(list)
        notifyDataSetChanged()
    }
}