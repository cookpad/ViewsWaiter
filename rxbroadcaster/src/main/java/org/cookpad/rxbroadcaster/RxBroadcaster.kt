package org.cookpad.rxbroadcaster

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class RxBroadcaster<T : Any>(bufferSize: Int = 100) {
    private val buffer = RxBroadcasterBuffer<T>(bufferSize)
    private val simpleSubject = PublishSubject.create<RxBroadcasterMessage<T>>()
    private val multipleSubject = BehaviorSubject.create<RxBroadcasterMessage<T>>()
    private val simpleObservable = simpleSubject.hide()
    private val multipleObservable = multipleSubject.hide()

    fun listen(getLast: Boolean = false): Observable<T> = getObservable(getLast).map { it.value }

    fun listen(filter: String, getLast: Boolean = false): Observable<T> = getObservable(getLast)
            .filter { it.key == filter }
            .map { it.value }

    fun getAll(): List<T> = buffer.all().map { it.value }

    fun emit(value: T, filter: String? = null) {
        RxBroadcasterMessage(filter ?: "", value).let {
            buffer.add(it)
            simpleSubject.onNext(it)
            multipleSubject.onNext(it)
        }
    }

    fun filter(filter: String) = object : RxFilteredBroadcaster<T> {
        override fun listen(getLast: Boolean) = this@RxBroadcaster.listen(filter, getLast)
        override fun emit(value: T) = this@RxBroadcaster.emit(value, filter)
    }

    private fun getObservable(getLast: Boolean) = if (getLast) multipleObservable else simpleObservable

    interface RxFilteredBroadcaster<T> {
        fun emit(value: T)
        fun listen(getLast: Boolean = false): Observable<T>
    }
}