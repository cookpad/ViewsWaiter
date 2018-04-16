package org.cookpad.rxbroadcaster.binders

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable
import io.reactivex.observables.ConnectableObservable

internal class OnBackgroundBinderTransformer<T>(lifecycle: Lifecycle) : ObservableTransformer<T, T>, LifecycleObserver {
    private var active = true
    private var dispose: Disposable? = null

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        active = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        active = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        dispose?.dispose()
    }

    override fun apply(upstream: Observable<T>): ConnectableObservable<T> = upstream
            .filter { active }
            .publish()
            .also { stream -> dispose = stream.connect() }
}

fun <T> Observable<T>.bindOnBackground(lifecycle: Lifecycle) = this.compose(OnBackgroundBinderTransformer(lifecycle))