package me.emredirican.datastream.data

sealed class PageLoadingState(
    open val error: Throwable?
) {
  object Initial : PageLoadingState(error = null)
  object Next : PageLoadingState(error = null)
  object Previous : PageLoadingState(error = null)
  object Completed : PageLoadingState(error = null)

  data class Error(
      override val error: Throwable? = null
  ): PageLoadingState(error)
}
