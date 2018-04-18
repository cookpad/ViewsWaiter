package org.cookpad.views_waiter.internal

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

internal class OnBackgroundBinderTransformer<T>(lifecycle: Lifecycle) : ObservableTransformer<T, T>, LifecycleObserver {
    private var isOnBackground = false

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        isOnBackground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        isOnBackground = true
    }

    override fun apply(upstream: Observable<T>): Observable<T> = upstream.filter { isOnBackground }
}

