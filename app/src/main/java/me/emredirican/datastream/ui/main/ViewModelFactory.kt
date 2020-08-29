package me.emredirican.datastream.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import me.emredirican.datastream.Main
import me.emredirican.datastream.domain.DataResult
import me.emredirican.datastream.domain.GetDataAction
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val getData: ObservableTransformer<GetDataAction, DataResult>,
    @Main private val mainScheduler: Scheduler
): ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return MainViewModel(getData, mainScheduler) as T
  }
}
