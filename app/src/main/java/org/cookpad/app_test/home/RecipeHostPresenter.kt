package org.cookpad.app_test.home

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.cookpad.app_test.utils.extensions.addTo

class RecipeHostPresenter(val view: View) : LifecycleObserver {
    private val compositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        view.apply {
            setupNavigation()

            onShowList.subscribe {
                showList()
            }.addTo(compositeDisposable)

            onShowBookmarks.subscribe {
                showBookmarks()
            }.addTo(compositeDisposable)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        compositeDisposable.dispose()
    }

    interface View {
        val onShowList: Observable<Unit>
        val onShowBookmarks: Observable<Unit>

        fun showList()
        fun showBookmarks()
        fun setupNavigation()
    }
}