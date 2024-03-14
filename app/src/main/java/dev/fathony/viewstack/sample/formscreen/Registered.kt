package dev.fathony.viewstack.sample.formscreen

import android.content.Context
import android.view.View
import dev.fathony.viewstack.sample.NameViewKey
import dev.fathony.viewstack.sample.databinding.ViewRegisteredBinding
import dev.fathony.viewstack.sample.view.Layout
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
class RegisteredKey : NameViewKey("Registered") {
    override fun buildView(context: Context): View = RegisteredView(context)
}

class RegisteredView(context: Context) : Layout<ViewRegisteredBinding>(context) {
    override val bindingClass: KClass<ViewRegisteredBinding>
        get() = ViewRegisteredBinding::class
}
