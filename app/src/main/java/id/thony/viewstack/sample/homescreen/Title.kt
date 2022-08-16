package id.thony.viewstack.sample.homescreen

import android.content.Context
import android.view.View
import id.thony.viewstack.sample.NameViewKey
import id.thony.viewstack.sample.databinding.ViewTitleBinding
import id.thony.viewstack.sample.navigator
import id.thony.viewstack.sample.view.Layout
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