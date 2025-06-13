import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// Stops collecting flow when view is destroyed
fun <T> Flow<T>.collectWhileCreated(owner: LifecycleOwner, collector: suspend (T) -> Unit) {
    // Launch coroutine tied to owners lifecycle scope
    owner.lifecycleScope.launch {
        // Repeat collection when lifecycle is in created state
        owner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
            collect { collector(it) }
        }
    }
}