package id.thony.viewstack.sample

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import id.thony.viewstack.Backstack
import id.thony.viewstack.DefaultBackstackHandler
import id.thony.viewstack.NavigationCommand
import id.thony.viewstack.Navigator

class ToolbarMultipleBackstackHandler(
    private val activity: AppCompatActivity,
    private val container: ViewGroup
) : DefaultBackstackHandler(activity, container) {

    override fun handleBackstackChange(
        navigator: Navigator,
        oldStack: Backstack,
        newStack: Backstack,
        command: NavigationCommand
    ) {
        updateToolbarState(newStack)

        when (command) {
            NavigationCommand.Replace -> {
                val topView = this.container.getChildAt(0)
                if (topView != null) {
                    val viewKey = getViewKey(topView)
                    saveViewState(topView, oldStack.obtainViewState(viewKey))
                }

                super.handleBackstackChange(navigator, oldStack, newStack, command)
            }
            NavigationCommand.Restore, NavigationCommand.Push, NavigationCommand.Pop -> {
                super.handleBackstackChange(navigator, oldStack, newStack, command)
            }
        }
    }

    private fun updateToolbarState(newStack: Backstack) {
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