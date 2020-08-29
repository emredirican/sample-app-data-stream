package me.emredirican.datastream.domain

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import me.emredirican.datastream.IO
import me.emredirican.datastream.Main
import me.emredirican.datastream.data.PageLoadingState
import me.emredirican.datastream.data.StatefulPagedKeyedDataSource
import me.emredirican.datastream.entity.Item
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    dataSourceFactory: StatefulPagedKeyedDataSource.Factory<Int, Item>,
    @IO fetchScheduler: Scheduler,
    @Main notifyScheduler: Scheduler
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
      { state, list ->
        return@combineLatest when (state) {
          PageLoadingState.Initial -> DataResult.Loading(list)

          PageLoadingState.Next -> DataResult.LoadingNextPage(list)

          PageLoadingState.Previous ->
            throw IllegalStateException("there should be no previous page loading state")

          PageLoadingState.Completed -> DataResult.Loaded(list)

          is PageLoadingState.Error -> DataResult.Error(list, state.error!!)
        }
      })
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

sealed class DataResult(
    open val pagedList: PagedList<Item>,
    open val error: Throwable? = null
) : Result {

  data class Loading(override val pagedList: PagedList<Item>) : DataResult(pagedList)

  data class LoadingNextPage(override val pagedList: PagedList<Item>) : DataResult(pagedList)

  data class Loaded(override val pagedList: PagedList<Item>) : DataResult(pagedList)

  data class Error(
      override val pagedList: PagedList<Item>,
      override val error: Throwable
  ) : DataResult(pagedList, error)
}
