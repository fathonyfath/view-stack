package id.thony.viewstack.sample.homescreen

import android.content.Context
import android.view.View
import id.thony.viewstack.ViewKey
import id.thony.viewstack.sample.databinding.ViewAboutBinding
import id.thony.viewstack.sample.view.Layout
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
class AboutKey : ViewKey() {
    override fun buildView(context: Context): View = AboutView(context)
}

class AboutView(context: Context) : Layout<ViewAboutBinding>(context) {
    override val bindingClass: KClass<ViewAboutBinding>
        get() = ViewAboutBinding::class
}