package id.thony.viewstack.sample.formscreen

import android.content.Context
import android.view.View
import id.thony.viewstack.sample.NameViewKey
import id.thony.viewstack.sample.databinding.ViewRegisterBinding
import id.thony.viewstack.sample.navigator
import id.thony.viewstack.sample.view.Layout
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
class RegisterKey : NameViewKey("Register") {
    override fun buildView(context: Context): View = RegisterView(context)
}

class RegisterView(context: Context) : Layout<ViewRegisterBinding>(context) {
    override val bindingClass: KClass<ViewRegisterBinding>
        get() = ViewRegisterBinding::class

    init {
        binding.signupBtn.setOnClickListener {
            navigator.push(RegisteredKey())
        }
    }
}