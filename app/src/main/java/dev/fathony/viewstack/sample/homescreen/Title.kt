package dev.fathony.viewstack.sample.homescreen

import android.content.Context
import android.view.View
import dev.fathony.viewstack.sample.NameViewKey
import dev.fathony.viewstack.sample.databinding.ViewTitleBinding
import dev.fathony.viewstack.sample.navigator
import dev.fathony.viewstack.sample.view.Layout
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
class TitleKey : NameViewKey("Home") {
    override fun buildView(context: Context): View = TitleView(context)
}

class TitleView(context: Context) : Layout<ViewTitleBinding>(context) {

    override val bindingClass: KClass<ViewTitleBinding>
        get() = ViewTitleBinding::class

    init {
        binding.aboutBtn.setOnClickListener {
            navigator.push(AboutKey())
        }
    }
}
