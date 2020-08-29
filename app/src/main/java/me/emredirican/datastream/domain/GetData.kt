package me.emredirican.datastream.domain

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import me.emredirican.datastream.data.PageLoadingState
import me.emredirican.datastream.data.StatefulPagedKeyedDataSource
import me.emredirican.datastream.entity.Item

class GetDataUseCase(
    private val dataSourceFactory: StatefulPagedKeyedDataSource.Factory<Int, Item>,
    private val fetchScheduler: Scheduler,
    private val notifyScheduler: Scheduler
) : ObservableTransformer<GetDataAction, DataResult> {

  private val config = PagedList.Config.Builder()
      .setPageSize(PAGE_SIZE)
      .setPrefetchDistance(PREFETCH_DISTANCE)
      .build()

  private val pagedListObservable = RxPagedListBuilder(dataSourceFactory, config)
      .setFetchScheduler(fetchScheduler)
      .setNotifyScheduler(notifyScheduler)
      .buildObservable()

  private val latestDataObservable = Observable.combineLatest(
      dataSourceFactory.dataSourceRelay.flatMap { it.pageLoadingStates },
      pagedListObservable,
      { state, list -> DataResult(pageLoadingState = state, items = list) })
      .publish()
      .autoConnect()

  override fun apply(upstream: Observable<GetDataAction>): ObservableSource<DataResult> {
    return upstream.flatMap { latestDataObservable }
  }

  companion object {
    const val PAGE_SIZE = 20
    const val PREFETCH_DISTANCE = 10
  }
}

object GetDataAction : Action

data class DataResult(
    val pageLoadingState: PageLoadingState,
    val items: PagedList<Item>
) : Result
