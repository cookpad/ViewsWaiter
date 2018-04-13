package org.cookpad.rxbroadcaster

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.cookpad.rxbroadcaster.rx_extensions.mapFirst

class RxStickyBroadcaster<T : Any>(bufferSize: Int = 300) {
    private val buffer = RxBroadcasterBuffer<T>(bufferSize)
    private val subject = BehaviorSubject.create<RxBroadcasterMessage<T>>()
    private val observable = subject.hide()

    private fun stream(filter: String): Observable<T> = observable
            .mapFirst { buffer.get(filter) ?: it }
            .filter { it.key == filter }
            .map { it.value }

    fun all(): Observable<List<T>> = Observable.just(buffer.all().map { it.value })

    private fun emit(value: T, filter: String) {
        RxBroadcasterMessage(filter, value)
                .let {
                    buffer.add(it)
                    subject.onNext(it)
                }
    }

    fun channel(filter: String) = object : RxFilteredBroadcaster<T> {
        override fun observe(getLast: Boolean) = this@RxStickyBroadcaster.stream(filter, getLast)
        override fun emit(value: T) = this@RxStickyBroadcaster.emit(value, filter)
    }

    interface RxFilteredBroadcaster<T> {
        fun emit(value: T)
        fun observe(getLast: Boolean = false): Observable<T>
    }
}