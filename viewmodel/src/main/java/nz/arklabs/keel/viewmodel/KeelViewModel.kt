package nz.arklabs.keel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.ReplayRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

open class KeelViewModel<
        S : KeelViewModel.State,
        E : KeelViewModel.Event,
        U : KeelViewModel.UiEvent
        >(initialState: S, reducer: Reducer<S, E>) : ViewModel() {

    private val disposables = CompositeDisposable()

    internal val eventsSubject: ReplayRelay<E> = ReplayRelay.create()
    internal val stateSubject: BehaviorRelay<S> = BehaviorRelay.createDefault(initialState)

    val uiEvents: SingleLiveEvent<U> = SingleLiveEvent()
    protected val events = eventsSubject.hide()

    init {
        disposables.add(eventsSubject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .scan(initialState) { state, event ->
                    try {
                        reducer.apply(state, event)
                    } catch (e: Exception) {
                        onReducerError(e)
                        state
                    }
                }
                .distinctUntilChanged()
                .subscribe(
                        { state -> stateSubject.accept(state) },
                        { e -> onReducerError(e) }
                )
        )
    }

    val state: Observable<S> = stateSubject.hide()
    val liveState: LiveData<S> = LiveDataReactiveStreams.fromPublisher(stateSubject.toFlowable(BackpressureStrategy.BUFFER))

    @Deprecated("Use convenience method", replaceWith = ReplaceWith("publish"))
    fun publishEvent(event: E) {
        publish(event)
    }

    fun publish(event: E) {
        eventsSubject.accept(event)
    }

    fun publish(event: U) {
        uiEvents.postValue(event)
    }

    @Deprecated("Use convenience method", replaceWith = ReplaceWith("publish"))
    fun publishUIEvent(event: U) {
        publish(event)
    }

    open fun onReducerError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    interface State

    interface UiEvent

    interface Event
}