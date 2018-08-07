package nz.arklabs.keel.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

inline fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    this.observe(owner, Observer { observer(it!!) })
}