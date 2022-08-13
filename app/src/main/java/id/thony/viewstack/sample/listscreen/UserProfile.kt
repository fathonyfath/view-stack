package id.thony.viewstack.sample.listscreen

import android.content.Context
import android.view.View
import id.thony.viewstack.sample.NameViewKey
import id.thony.viewstack.sample.databinding.ViewUserProfileBinding
import id.thony.viewstack.sample.getKey
import id.thony.viewstack.sample.view.Layout
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