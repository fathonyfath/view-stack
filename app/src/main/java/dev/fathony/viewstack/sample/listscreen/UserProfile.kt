package dev.fathony.viewstack.sample.listscreen

import android.content.Context
import android.view.View
import dev.fathony.viewstack.sample.NameViewKey
import dev.fathony.viewstack.sample.databinding.ViewUserProfileBinding
import dev.fathony.viewstack.sample.getKey
import dev.fathony.viewstack.sample.view.Layout
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
class UserProfileKey(val userName: String) : NameViewKey("Profile") {
    override fun buildView(context: Context): View = UserProfileView(context)
}

class UserProfileView(context: Context) : Layout<ViewUserProfileBinding>(context) {
    override val bindingClass: KClass<ViewUserProfileBinding>
        get() = ViewUserProfileBinding::class

    init {
        val key = getKey<UserProfileKey>()
        binding.profileUserName.text = key.userName
    }
}
