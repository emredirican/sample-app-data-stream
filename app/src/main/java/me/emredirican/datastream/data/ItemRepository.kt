package me.emredirican.datastream.data

import io.reactivex.Observable
import me.emredirican.datastream.entity.Item

class ItemRepository(

): StatefulPagedKeyedDataSource<Int, Item>() {

  override val pageLoadingStates: Observable<PageLoadingState>
    get() = TODO("Not yet implemented")

  override fun loadInitial(params: LoadInitialParams<Int>,
      callback: LoadInitialCallback<Int, Item>) {
    TODO("Not yet implemented")
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
    TODO("Not yet implemented")
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
    TODO("Not yet implemented")
  }
}
