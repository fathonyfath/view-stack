package id.thony.viewstack.sample.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import kotlin.reflect.KClass

abstract class Layout<T : ViewBinding>(context: Context) : FrameLayout(context) {
    abstract val bindingClass: KClass<T>

    private val layoutInflater = LayoutInflater.from(context)

    protected val binding: T

    init {
        binding = instantiateBindingClass()
    }

    @Suppress("UNCHECKED_CAST")
    private fun instantiateBindingClass(): T {
        val inflateMethod = findInflateMethods()
            ?: throw IllegalStateException("bindingClass does not have ${bindingClass.simpleName} inflate(LayoutInflater, ViewGroup, Boolean) method")

        return inflateMethod.invoke(null, layoutInflater, this, true) as T
    }

    private fun findInflateMethods(): Method? {
        return bindingClass.java.methods
            .filterNotNull()
            .firstOrNull { method ->
                method.parameterTypes.size == 3
                        && LayoutInflater::class.java.isAssignableFrom(method.parameterTypes[0])
                        && ViewGroup::class.java.isAssignableFrom(method.parameterTypes[1])
                        && Boolean::class.java.isAssignableFrom(method.parameterTypes[2])
            }
    }
}