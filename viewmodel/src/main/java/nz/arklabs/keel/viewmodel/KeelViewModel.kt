package nz.arklabs.keel.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

open class KeelViewModel<
        S : KeelViewModel.State,
        E : KeelViewModel.Event,
        U : KeelViewModel.UiEvent
        >(initialState: S, reducer: Reducer<S, E>) : ViewModel() {

    val state: MutableLiveData<S> = MutableLiveData()
    val uiEvents: SingleLiveEvent<U> = SingleLiveEvent()

    internal val events: PublishSubject<E> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    init {
        state.value = initialState

        compositeDisposable.add(events
                .observeOn(Schedulers.io())
                .scan(initialState, { state, event -> reducer.apply(state, event) })
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ state.value = it }))
    }

    fun publishEvent(event: E) {
        events.onNext(event)
    }

    fun publishUIEvent(event: U) {
        uiEvents.postValue(event)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    interface State

    interface UiEvent

    interface Event
}