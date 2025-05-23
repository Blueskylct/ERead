package com.blueskylct.eread.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.blueskylct.eread.data.repository.Repository
import com.blueskylct.eread.databinding.ItemBookBinding
import com.blueskylct.eread.domain.model.CacheBook
import com.blueskylct.eread.ui.home.HomeActivity
import com.blueskylct.eread.ui.reading.ReadingActivity
import com.blueskylct.eread.utils.EpubUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BookListAdapter(private val bookList: ArrayList<CacheBook>, private val activity: HomeActivity):
    RecyclerView.Adapter<BookListAdapter.BookListViewHolder>() {

    inner class BookListViewHolder(binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root){
        val frontCover = binding.frontCover
        val title = binding.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return BookListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        val book = bookList[position]
        val bitmap = EpubUtil.loadEpubCoverImage(book.uri.toUri())
        holder.frontCover.setImageBitmap(bitmap)
        holder.title.text = book.title
        holder.itemView.setOnClickListener {
            if (EpubUtil.loadEpub(book.uri.toUri())) {
                activity.startActivity(Intent(activity, ReadingActivity::class.java))
            }
        }

        holder.itemView.setOnLongClickListener {
            MaterialAlertDialogBuilder(activity)
                .setTitle("注意")
                .setMessage("你确认要删除这本书吗？")
                .setPositiveButton("确认") { _,_ ->
                    bookList.remove(book)
                    runBlocking {
                        launch {
                            Repository.getInstance().deleteBook(book)
                        }
                    }
                    notifyDataSetChanged()
                }
                .setNegativeButton("取消"){
                    _, _ -> //TODO
                }
                .show()
            true
        }
    }

    //更新书籍list
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: ArrayList<CacheBook>){
        bookList.clear()
        bookList.addAll(newList)
        notifyDataSetChanged()
    }
}