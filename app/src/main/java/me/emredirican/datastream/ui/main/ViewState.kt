package me.emredirican.datastream.ui.main

import androidx.paging.PagedList
import me.emredirican.datastream.entity.Item

data class ViewState(
    val items: PagedList<Item>?
)
