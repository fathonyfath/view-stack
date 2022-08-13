package id.thony.viewstack.sample.formscreen

import android.content.Context
import android.view.View
import id.thony.viewstack.ViewKey
import id.thony.viewstack.sample.databinding.ViewRegisteredBinding
import id.thony.viewstack.sample.view.Layout
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
class RegisteredKey : ViewKey() {
    override fun buildView(context: Context): View = RegisteredView(context)
}

class RegisteredView(context: Context) : Layout<ViewRegisteredBinding>(context) {
    override val bindingClass: KClass<ViewRegisteredBinding>
        get() = ViewRegisteredBinding::class
}