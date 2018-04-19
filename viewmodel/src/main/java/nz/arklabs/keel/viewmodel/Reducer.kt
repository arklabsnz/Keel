package nz.arklabs.keel.viewmodel

interface Reducer<S, in E> {
    fun apply(state: S, event: E): S
}