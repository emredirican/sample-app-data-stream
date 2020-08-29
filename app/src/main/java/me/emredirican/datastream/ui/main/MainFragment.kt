package me.emredirican.datastream.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.main_fragment.*
import me.emredirican.datastream.R
import me.emredirican.datastream.ui.Event

class MainFragment : Fragment(), me.emredirican.datastream.ui.View {

  private val eventRelay = PublishSubject.create<Event>()
  private val adapter = DataAdapter()
  private val onPauseDisposables = CompositeDisposable()

  private lateinit var viewModel: MainViewModel

  override fun events(): Observable<Event> {
    return eventRelay
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.main_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rv_items.adapter = adapter
  }

  override fun onResume() {
    super.onResume()

    onPauseDisposables.add(
        viewModel.state(events())
            .subscribe(
                { adapter.submitList(it.items) },
                { Log.e("MainFragment", it.localizedMessage, it) }
            )
    )
  }

  override fun onPause() {
    onPauseDisposables.clear()
    super.onPause()
  }

  companion object {
    fun newInstance() = MainFragment()
  }
}
