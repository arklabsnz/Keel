package nz.arklabs.keel.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    this.observe(owner, Observer { observer(it!!) })
}