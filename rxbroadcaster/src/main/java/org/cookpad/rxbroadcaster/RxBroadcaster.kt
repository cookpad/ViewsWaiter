package org.cookpad.rxbroadcaster

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.cookpad.rxbroadcaster.rx_extensions.mapFirst

class RxBroadcaster<T : Any>(bufferSize: Int = 300) {
    private val buffer = RxBroadcasterBuffer<T>(bufferSize)
    private val simpleSubject = PublishSubject.create<RxBroadcasterMessage<T>>()
    private val multipleSubject = BehaviorSubject.create<RxBroadcasterMessage<T>>()
    private val simpleObservable = simpleSubject.hide()
    private val multipleObservable = multipleSubject.hide()

    fun observe(getLast: Boolean = false): Observable<T> = getObservable(getLast).map { it.value }

    fun observe(filter: String, getLast: Boolean = false): Observable<T> = getObservable(getLast)
            .mapFirst {
                if (getLast && buffer.get(filter) != null) {
                    buffer.get(filter)
                } else {
                    it
                }
            }
            .filter { it.key == filter }
            .map { it.value }

    fun all(): Observable<List<T>> = Observable.just(buffer.all().map { it.value })

    fun onNext(value: T, filter: String? = null) {
        RxBroadcasterMessage(filter ?: value.hashCode().toString(), value).let {
            buffer.add(it)
            simpleSubject.onNext(it)
            multipleSubject.onNext(it)
        }
    }

    fun filter(filter: String) = object : RxFilteredBroadcaster<T> {
        override fun observe(getLast: Boolean) = this@RxBroadcaster.observe(filter, getLast)
        override fun onNext(value: T) = this@RxBroadcaster.onNext(value, filter)
    }

    private fun getObservable(getLast: Boolean) = if (getLast) multipleObservable else simpleObservable

    interface RxFilteredBroadcaster<T> {
        fun onNext(value: T)
        fun observe(getLast: Boolean = false): Observable<T>
    }
}