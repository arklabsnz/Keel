package nz.arklabs.viewmodel

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.mock
import nz.arklabs.keel.viewmodel.KeelViewModel

fun <S : KeelViewModel.State, E : KeelViewModel.Event, U : KeelViewModel.UiEvent> KeelViewModel<S, E, U>.observeUiEvents(): Observer<U> {
    val observer = mock<Observer<U>>()
    uiEvents.observeForever(observer)
    return observer
}

fun <S : KeelViewModel.State, E : KeelViewModel.Event, U : KeelViewModel.UiEvent> KeelViewModel<S, E, U>.observeState(): Observer<S> {
    val observer = mock<Observer<S>>()
    liveState.observeForever(observer)
    return observer
}
