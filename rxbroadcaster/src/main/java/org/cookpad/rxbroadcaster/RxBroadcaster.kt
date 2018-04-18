package org.cookpad.rxbroadcaster

import android.arch.lifecycle.Lifecycle
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.cookpad.rxbroadcaster.internal.OnBackgroundBinderTransformer
import org.cookpad.rxbroadcaster.internal.RxBroadcasterMessage

/**
 * A simple reactive pipeline for emitting and subscribing on one single stream.
 */
class RxBroadcaster<T : Any> {
    private val defaultChannelName = ""
    private val subject = PublishSubject.create<RxBroadcasterMessage<T>>()
    private val observable = subject.hide()

    /**
     * Call it when the subscriber wants to receive all events without being filtered by any channel.
     */
    fun stream(): Observable<T> = observable.map { it.value }

    /**
     * Call it when events wants to be emitted without channel.
     */
    fun emit(value: T) {
        subject.onNext(RxBroadcasterMessage(defaultChannelName, value))
    }

    private fun stream(channel: String): Observable<T> = observable
            .filter { it.key == channel }
            .map { it.value }

    private fun emit(value: T, channel: String = defaultChannelName) {
        subject.onNext(RxBroadcasterMessage(channel, value))
    }

    /**
     * Create a channel with a name, use it when emitting and subscribing needs to be performed in an specific channel
     */
    fun channel(name: String) = object : RxChannel<T> {
        override fun stream() = this@RxBroadcaster.stream(name)
        override fun emit(value: T) = this@RxBroadcaster.emit(value, name)
    }
}

/**
 * A convenience extension function for syncing an observable with lifecycle events in a very specific way:
 * Filter those emissions received when the component is in foreground, in other words, upstream only receives events if it's on background.
 */
fun <T> Observable<T>.bindOnBackground(lifecycle: Lifecycle) = this.compose(OnBackgroundBinderTransformer(lifecycle))