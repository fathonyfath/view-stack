package dev.fathony.viewstack.sample.homescreen

import android.content.Context
import android.view.View
import dev.fathony.viewstack.sample.NameViewKey
import dev.fathony.viewstack.sample.databinding.ViewAboutBinding
import dev.fathony.viewstack.sample.view.Layout
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
class AboutKey : NameViewKey("About") {
    override fun buildView(context: Context): View = AboutView(context)
}

class AboutView(context: Context) : Layout<ViewAboutBinding>(context) {
    override val bindingClass: KClass<ViewAboutBinding>
        get() = ViewAboutBinding::class
}
