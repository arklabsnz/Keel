package nz.arklabs.keel.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

open class KeelViewModel<
        S : KeelViewModel.State,
        E : KeelViewModel.Event,
        U : KeelViewModel.UiEvent
        >(initialState: S, reducer: Reducer<S, E>) : ViewModel() {

    internal val eventsSubject: PublishSubject<E> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()
    val state: MutableLiveData<S> = MutableLiveData()
    val uiEvents: SingleLiveEvent<U> = SingleLiveEvent()
    protected val events = eventsSubject as Observable<E>

    init {
        state.value = initialState

        compositeDisposable.add(eventsSubject
                .observeOn(Schedulers.io())
                .scan(initialState, { state, event -> reducer.apply(state, event) })
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ state.value = it }))
    }

    fun publishEvent(event: E) {
        eventsSubject.onNext(event)
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