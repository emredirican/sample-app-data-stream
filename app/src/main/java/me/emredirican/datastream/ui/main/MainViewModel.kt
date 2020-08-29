package me.emredirican.datastream.ui.main

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import me.emredirican.datastream.domain.Action
import me.emredirican.datastream.domain.DataResult
import me.emredirican.datastream.domain.FilterAction
import me.emredirican.datastream.domain.FilterResult
import me.emredirican.datastream.domain.GetDataAction
import me.emredirican.datastream.domain.Result
import me.emredirican.datastream.ui.Event

//TODO error handling
class MainViewModel(
    private val getData: ObservableTransformer<GetDataAction, DataResult>,
    private val filter: ObservableTransformer<FilterAction, FilterResult>,
    private val mainScheduler: Scheduler
) : ViewModel() {

  private val statePublisher = BehaviorSubject.createDefault(ViewState(null))

  private val submit = ObservableTransformer<Action, Result> { upstream ->
    upstream.publish { sharedAction ->
      Observable.merge(listOf(
          sharedAction.ofType(GetDataAction::class.java).compose(getData),
          sharedAction.ofType(FilterAction::class.java).compose(filter)
      ))
    }
  }

  fun state(eventObservable: Observable<Event>): Observable<ViewState> {
    return eventObservable.map(::toAction)
        .startWith(GetDataAction)
        .compose(submit)
        .scan(statePublisher.value!!, ::reduceState)
        .observeOn(mainScheduler)
        .distinctUntilChanged()
  }

  private fun reduceState(previous: ViewState, result: Result): ViewState {
    return when (result) {
      is DataResult -> ViewState(result.pagedList)
      else -> previous
    }
  }

  private fun toAction(event: Event): Action {
    return when (event) {
      is FilterResults -> FilterAction(event.rating)
      else -> object : Action {}
    }
  }
}

data class FilterResults(val rating: Int) : Event
