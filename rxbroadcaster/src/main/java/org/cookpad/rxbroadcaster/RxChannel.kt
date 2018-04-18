package org.cookpad.rxbroadcaster

import io.reactivex.Observable

/**
 * This interface encodes the behaviour of a channel emitting and subscribing events filtered by the name of the channel.
 */
interface RxChannel<T> {

    /**
     * Call it when events wants to be emitted on this channel.
     */
    fun emit(value: T)

    /**
     * Call it when the subscriber wants to receive events from this specific channel.
     */
    fun stream(): Observable<T>
}