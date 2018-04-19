package nz.arklabs.viewmodel

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import org.mockito.Mockito


inline fun <reified S : Any> Observer<S>.verifyNever() {
    verify(this, never()).onChanged(any())
}

inline fun <reified S : Any> Observer<S>.verify(invocations: Int) {
    verify(this, times(invocations)).onChanged(any())
}

inline fun <reified S : Any> Observer<S>.clearInvocations() {
    Mockito.clearInvocations(this)
}

inline fun <reified T : Any> Observer<T>.capture(invocations: Int? = null): KArgumentCaptor<T> {
    val arg = argumentCaptor<T>()
    if (invocations != null) {
        verify(this, times(invocations)).onChanged(arg.capture())
    } else {
        verify(this, atLeast(1)).onChanged(arg.capture())
    }
    return arg
}

inline fun <reified T : Any> Observer<T>.last(): T {
    return this.capture().lastValue
}

inline val <reified T : Any> Observer<T>.last: T
    get() = this.capture().lastValue

inline val <reified T : Any> Observer<T>.first: T
    get() = this.capture().firstValue

inline val <reified T : Any> Observer<T>.values: List<T>
    get() = this.capture().allValues
