package org.cookpad.rxbroadcaster.rx_extensions

import io.reactivex.*
import io.reactivex.disposables.Disposable

internal class MapFirst<T>(val num: Int, val mapper: (T) -> T) : ObservableOperator<T, T> {
    var count = 0
    override fun apply(observer: Observer<in T>): Observer<in T> {
        return object : Observer<T> {
            override fun onComplete() {
                observer.onComplete()
            }

            override fun onSubscribe(d: Disposable) {
                observer.onSubscribe(d)
            }

            override fun onNext(t: T) {
                if (count < num) {
                    val mapped = mapper(t)
                    observer.onNext(mapped)
                } else {
                    observer.onNext(t)
                }
                count++
            }

            override fun onError(e: Throwable) {
                observer.onError(e)
            }

        }
    }
}

internal fun <T> Observable<T>.mapFirst(count: Int = 1, mapper: (T) -> T): Observable<T> = this.lift(MapFirst(count, mapper))