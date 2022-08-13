package id.thony.viewstack.sample

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import id.thony.viewstack.Backstack
import id.thony.viewstack.DefaultBackstackHandler
import id.thony.viewstack.NavigationDirection
import id.thony.viewstack.Navigator

class ToolbarBackstackHandler(
    private val activity: AppCompatActivity,
    container: ViewGroup
) : DefaultBackstackHandler(activity, container) {

    override fun handleBackstackChange(
        navigator: Navigator,
        oldStack: Backstack,
        newStack: Backstack,
        direction: NavigationDirection
    ) {
        super.handleBackstackChange(navigator, oldStack, newStack, direction)

        val key = newStack.peekKey() as? NameViewKey
        if (key != null) {
            setTitle(key.name)
        }

        if (newStack.count() == 1) {
            setNavigationIcon(null, 0)
        } else {
            val drawerArrowDrawable = DrawerArrowDrawable(activity)
            drawerArrowDrawable.progress = 1.0f
            setNavigationIcon(drawerArrowDrawable, R.string.navigate_up_description)
        }
    }

    private fun setTitle(title: CharSequence) {
        activity.supportActionBar?.title = title
    }

    private fun setNavigationIcon(icon: Drawable?, @StringRes contentDescription: Int) {
        if (icon == null) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.drawerToggleDelegate?.setActionBarDescription(contentDescription)
        }
    }
}