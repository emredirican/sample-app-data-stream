package me.emredirican.datastream.data

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

abstract class StatefulPagedKeyedDataSource<Key, Value> : PageKeyedDataSource<Key, Value>() {

  abstract val pageLoadingStates: Observable<PageLoadingState>

  abstract class Factory<Key, Value> : DataSource.Factory<Key, Value>() {

    val dataSourceRelay: BehaviorSubject<StatefulPagedKeyedDataSource<Key, Value>> =
        BehaviorSubject.create()

    abstract val creator: () -> StatefulPagedKeyedDataSource<Key, Value>

    final override fun create(): StatefulPagedKeyedDataSource<Key, Value> {
      val dataSource = creator.invoke()
      dataSourceRelay.onNext(dataSource)
      return dataSource
    }
  }
}
