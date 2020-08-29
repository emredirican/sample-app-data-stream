package me.emredirican.datastream.domain

import me.emredirican.datastream.data.ItemRepository
import me.emredirican.datastream.data.StatefulPagedKeyedDataSource
import me.emredirican.datastream.entity.Item

class DataSourceFactory(
    private val itemRepository: ItemRepository
): StatefulPagedKeyedDataSource.Factory<Int, Item>() {

  override val creator: () -> StatefulPagedKeyedDataSource<Int, Item>
    get() = { itemRepository }
}
