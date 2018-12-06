package nz.arklabs.keel.viewmodel.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ViewModelInjectionFactory<Type : ViewModel> @Inject constructor(private val viewModel: dagger.Lazy<Type>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(p0: Class<T>): T {
        return viewModel.get() as T
    }
}