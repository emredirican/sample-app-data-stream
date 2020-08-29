package me.emredirican.datastream.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.emredirican.datastream.entity.Item
import java.io.InputStreamReader

class ItemRepository(
    //TODO replace with an interface
    context: Context,
    private val gson: Gson,
    private var ratingFilter: Int? = null
) : StatefulPagedKeyedDataSource<Int, Item>() {

  private val inputStream = context.assets.open("apidemo.json")
  private val loadingStateRelay = PublishSubject.create<PageLoadingState>()
  private val firstPageNum = 0
  private var currentReader: JsonReader? = null
  private var curItemPos = 0
  private var readerClosed = false

  override val pageLoadingStates: Observable<PageLoadingState>
    get() = loadingStateRelay

  override fun loadInitial(params: LoadInitialParams<Int>,
      callback: LoadInitialCallback<Int, Item>) {

    loadingStateRelay.onNext(PageLoadingState.Initial)

    val loadSize = params.requestedLoadSize
    currentReader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
    currentReader!!.beginObject()
    currentReader!!.nextName()
    currentReader!!.beginArray()
    val items = mutableListOf<Item>()
    curItemPos = 0
    while (currentReader!!.hasNext() && curItemPos < loadSize) {
      addItemAndMoveCursor(items)
    }
    closeReaderIfNecessary()
    callback.onResult(items, null, firstPageNum + 1)
    loadingStateRelay.onNext(PageLoadingState.Completed)
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
    //not using
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {

    loadingStateRelay.onNext(PageLoadingState.Next)
    if(readerClosed){
      callback.onResult(emptyList(), params.key + 1)
      loadingStateRelay.onNext(PageLoadingState.Completed)
    } else {
      val startPos = curItemPos
      val items = mutableListOf<Item>()
      while (currentReader!!.hasNext() && curItemPos < startPos + params.requestedLoadSize) {
        addItemAndMoveCursor(items)
      }
      closeReaderIfNecessary()
      callback.onResult(items, params.key + 1)
      loadingStateRelay.onNext(PageLoadingState.Completed)
    }


  }

  private fun addItemAndMoveCursor(items: MutableList<Item>) {
    val item = gson.fromJson<Item>(currentReader, Item::class.java)
    if (ratingFilter == null) {
      items.add(item)
    } else {
      if (item.rating == ratingFilter) {
        items.add(item)
      }
    }
    curItemPos++
  }

  private fun closeReaderIfNecessary() {
    currentReader?.apply {
      if(!hasNext()){
        endArray()
        close()
        readerClosed = true
      }
    }
  }
}
