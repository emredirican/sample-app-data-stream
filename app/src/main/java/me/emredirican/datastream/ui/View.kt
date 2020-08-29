package me.emredirican.datastream.ui

import io.reactivex.Observable

interface View {

  fun events(): Observable<Event>
}
