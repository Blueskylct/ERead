package com.blueskylct.eread.ui.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueskylct.eread.databinding.ItemBookBinding
import com.blueskylct.eread.domain.model.CacheBook
import nl.siegmann.epublib.domain.Book

class BookListAdapter(private val bookList: ArrayList<CacheBook>): RecyclerView.Adapter<BookListAdapter.BookListViewHolder>() {

    inner class BookListViewHolder(binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root){
        val frontCover = binding.frontCover
        val title = binding.title
        val introduction = binding.introduction
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return BookListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        val book = bookList[position]
        //val bitmap = BitmapFactory.decodeStream(book.coverImage.inputStream)
        //holder.frontCover.setImageBitmap(bitmap)
        holder.title.text = book.title
        holder.introduction.text = book.introduction
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: ArrayList<CacheBook>){
        bookList.clear()
        bookList.addAll(newList)
        notifyDataSetChanged()
    }
}