package me.emredirican.datastream.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.emredirican.datastream.entity.Item

class DataAdapter : PagedListAdapter<Item, ItemViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    TODO("Not yet implemented")
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    TODO("Not yet implemented")
  }

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
      override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
      }
    }
  }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}

