package org.cookpad.rxbroadcaster

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxBroadcaster<T : Any> {
    private val defaultChannelName = ""
    private val subject = PublishSubject.create<RxBroadcasterMessage<T>>()
    private val observable = subject.hide()

    fun stream(): Observable<T> = observable.map { it.value }

    private fun stream(channel: String): Observable<T> = observable
            .filter { it.key == channel }
            .map { it.value }

    fun emit(value: T) {
        subject.onNext(RxBroadcasterMessage(defaultChannelName, value))
    }

    private fun emit(value: T, channel: String = defaultChannelName) {
        subject.onNext(RxBroadcasterMessage(channel, value))
    }

    fun channel(name: String) = object : RxChannel<T> {
        override fun stream() = this@RxBroadcaster.stream(name)
        override fun emit(value: T) = this@RxBroadcaster.emit(value, name)
    }

    interface RxChannel<T> {
        fun emit(value: T)
        fun stream(): Observable<T>
    }
}