package nz.arklabs.keel.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

open class KeelViewModel<
        S : KeelViewModel.State,
        E : KeelViewModel.Event,
        U : KeelViewModel.UiEvent
        >(initialState: S, reducer: Reducer<S, E>) : ViewModel() {

    private val disposables = CompositeDisposable()

    internal val eventsSubject: ReplaySubject<E> = ReplaySubject.create()
    internal val stateSubject: BehaviorSubject<S> = BehaviorSubject.createDefault(initialState)

    val uiEvents: SingleLiveEvent<U> = SingleLiveEvent()
    protected val events = eventsSubject as Observable<E>

    init {
        disposables.add(eventsSubject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .scan(initialState) { state, event -> reducer.apply(state, event) }
                .distinctUntilChanged()
                .doOnError { it.printStackTrace() }
                .subscribe { stateSubject.onNext(it) })
    }

    val state: Observable<S> = stateSubject.hide()
    val liveState: LiveData<S> = LiveDataReactiveStreams.fromPublisher(stateSubject.toFlowable(BackpressureStrategy.BUFFER))

    fun publishEvent(event: E) {
        eventsSubject.onNext(event)
    }

    fun publishUIEvent(event: U) {
        uiEvents.postValue(event)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    interface State

    interface UiEvent

    interface Event
}