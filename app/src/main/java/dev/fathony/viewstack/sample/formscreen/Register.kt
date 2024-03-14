package dev.fathony.viewstack.sample.formscreen

import android.content.Context
import android.view.View
import dev.fathony.viewstack.sample.NameViewKey
import dev.fathony.viewstack.sample.databinding.ViewRegisterBinding
import dev.fathony.viewstack.sample.navigator
import dev.fathony.viewstack.sample.view.Layout
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
