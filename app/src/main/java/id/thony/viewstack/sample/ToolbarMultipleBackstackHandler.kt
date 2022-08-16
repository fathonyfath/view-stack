package id.thony.viewstack.sample

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import id.thony.viewstack.Backstack
import id.thony.viewstack.DefaultBackstackHandler
import id.thony.viewstack.NavigationCommand
import id.thony.viewstack.Navigator

class ToolbarMultipleBackstackHandler(
    private val activity: AppCompatActivity,
    private val container: ViewGroup,
    private val animationContainer: ViewGroup
) : DefaultBackstackHandler(activity, container) {

    override fun handleBackstackChange(
        navigator: Navigator,
        oldStack: Backstack,
        newStack: Backstack,
        command: NavigationCommand
    ) {
        updateToolbarState(newStack)

        if (command == NavigationCommand.Replace) {
            val topView = this.container.getChildAt(0)
            if (topView != null) {
                val viewKey = getViewKey(topView)
                saveViewState(topView, oldStack.obtainViewState(viewKey))
                this.container.removeView(topView)
                this.animationContainer.addView(topView)
                val fadeOutAnim = createFadeOutAnimation(this.animationContainer, topView)
                topView.startAnimation(fadeOutAnim)
            }
            val upcomingKey = newStack.peekKey()
            val upcomingViewState = newStack.obtainViewState(upcomingKey)
            val view = buildView(upcomingKey)
            restoreViewState(view, upcomingViewState)
            val fadeInAnim = createFadeInAnimation()

            this.container.addView(view)
            view.startAnimation(fadeInAnim)
        } else if (command == NavigationCommand.Restore) {
            this.animationContainer.removeAllViews()
            super.handleBackstackChange(navigator, oldStack, newStack, command)
        }

        if (command == NavigationCommand.Pop || command == NavigationCommand.Push) {
            super.handleBackstackChange(navigator, oldStack, newStack, command)
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

    private fun createFadeOutAnimation(parent: ViewGroup, view: View): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.fade_out).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(anim: Animation) = Unit

                override fun onAnimationEnd(anim: Animation) {
                    parent.removeView(view)
                }

                override fun onAnimationRepeat(anim: Animation) = Unit
            })
        }
    }

    private fun createFadeInAnimation(): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.fade_in)
    }
}