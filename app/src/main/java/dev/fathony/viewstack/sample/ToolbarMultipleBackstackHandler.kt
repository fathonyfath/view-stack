package dev.fathony.viewstack.sample

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import dev.fathony.viewstack.Backstack
import dev.fathony.viewstack.DefaultBackstackHandler
import dev.fathony.viewstack.NavigationCommand
import dev.fathony.viewstack.Navigator

class ToolbarMultipleBackstackHandler(
    private val activity: AppCompatActivity,
    container: ViewGroup
) : DefaultBackstackHandler(activity, container) {

    override fun handleBackstackChange(
        navigator: Navigator,
        oldStack: Backstack,
        newStack: Backstack,
        command: NavigationCommand
    ) {
        updateToolbarState(newStack)
        super.handleBackstackChange(navigator, oldStack, newStack, command)
    }

    override fun enterAnimation(): Animation? {
        return AnimationUtils.loadAnimation(activity, R.anim.fade_in)
    }

    override fun exitAnimation(): Animation? {
        return AnimationUtils.loadAnimation(activity, R.anim.fade_out)
    }

    override fun popEnterAnimation(): Animation? {
        return AnimationUtils.loadAnimation(activity, R.anim.pop_slide_in)
    }

    override fun popExitAnimation(): Animation? {
        return AnimationUtils.loadAnimation(activity, R.anim.pop_slide_out)
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
