package org.cookpad.rxbroadcaster

import io.reactivex.Observable

interface RxChannel<T> {
    fun emit(value: T)
    fun stream(): Observable<T>
}