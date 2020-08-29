package me.emredirican.datastream.domain

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class FilterUseCase @Inject constructor(
    private val dataSourceFactory: DataSourceFactory
) : ObservableTransformer<FilterAction, FilterResult> {

  override fun apply(upstream: Observable<FilterAction>): ObservableSource<FilterResult> {
    return upstream.doOnNext { action ->
      dataSourceFactory.ratingFilter = action.rating
      dataSourceFactory.dataSourceRelay.value!!.invalidate()
    }.map { FilterResult }
  }
}

data class FilterAction(val rating: Int) : Action

object FilterResult : Result
