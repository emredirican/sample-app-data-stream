package me.emredirican.datastream.domain

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import me.emredirican.datastream.data.ItemRepository
import me.emredirican.datastream.data.StatefulPagedKeyedDataSource
import me.emredirican.datastream.entity.Item

class DataSourceFactory(
    @ApplicationContext private val context: Context,
    private val gson: Gson,
    var ratingFilter: Int? = null
) : StatefulPagedKeyedDataSource.Factory<Int, Item>() {

  override val creator: () -> StatefulPagedKeyedDataSource<Int, Item>
    get() = { ItemRepository(context, gson, ratingFilter) }
}
