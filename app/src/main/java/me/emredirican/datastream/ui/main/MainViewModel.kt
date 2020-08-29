package me.emredirican.datastream.ui.main

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.subjects.BehaviorSubject
import me.emredirican.datastream.domain.Action
import me.emredirican.datastream.domain.ListHolder
import me.emredirican.datastream.domain.GetDataAction
import me.emredirican.datastream.ui.Event
import javax.xml.transform.Result

class MainViewModel(
    private val getData: ObservableTransformer<GetDataAction, ListHolder>,
    private val mainScheduler: Scheduler
) : ViewModel() {

  private val statePublisher = BehaviorSubject.create<ViewState>()

  private val submit = ObservableTransformer<Action, Result> { upstream ->
    upstream.publish { sharedAction ->
      Observable.merge(listOf(
          sharedAction.ofType(GetDataAction::class.java).compose(getData)
      ))
          .cast(Result::class.java)
    }
  }

  fun state(eventObservable: Observable<Event>): Observable<ViewState> {
    return eventObservable.map(::toAction)
        .compose(submit)
        .scan(statePublisher.value!!, ::reduceState)
        .observeOn(mainScheduler)
        .distinctUntilChanged()
  }

  private fun reduceState(previous: ViewState, result: Result): ViewState {
    return ViewState()
  }

  private fun toAction(event: Event): Action {
    return object : Action {}
  }
}
