package me.emredirican.datastream.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.view.*
import me.emredirican.datastream.R
import me.emredirican.datastream.entity.Item

class DataAdapter : PagedListAdapter<Item, ItemViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
    return ItemViewHolder(view)
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.bind(getItem(position)!!)
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

class ItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(
    containerView), LayoutContainer {

  fun bind(item: Item) {
    containerView.tv_item_id.text = item.id
  }
}

