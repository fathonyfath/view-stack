package id.thony.viewstack.sample

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import id.thony.viewstack.*

class ToolbarMultipleBackstackHandler(
    private val activity: AppCompatActivity,
    private val container: ViewGroup
) : DefaultBackstackHandler(activity, container) {

    override fun handleBackstackChange(
        navigator: Navigator,
        oldStack: Backstack,
        newStack: Backstack,
        direction: NavigationDirection,
        restoreState: Boolean
    ) {
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

        if (direction == NavigationDirection.Replace) {
            val upcomingKey = newStack.peekKey()
            val upcomingViewState = newStack.obtainViewState(upcomingKey)
            val context = ViewKeyContextWrapper(activity, upcomingKey)
            val upcomingView = upcomingKey.buildView(context)
            restoreViewState(upcomingView, upcomingViewState)

            if (restoreState) {
                container.removeAllViews()
                container.addView(upcomingView)
            } else {
                if (oldStack.peekKey() != newStack.peekKey()) {
                    // Backstack is changing, assume the current view on container is top of old stack
                    val oldView = container.getChildAt(0) ?: throw IllegalStateException()
                    saveViewState(oldView, oldStack.obtainViewState(oldStack.peekKey()))
                    val anim = createFadeOutAnimation(container, oldView)
                    oldView.startAnimation(anim)
                } else {
                    container.removeAllViews()
                }

                container.addView(upcomingView)
                upcomingView.startAnimation(createFadeInAnimation())
            }
            return
        }

        if (oldStack.peekKey() !== newStack.peekKey()) {
            val oldView = container.getChildAt(0) ?: return
            if (direction == NavigationDirection.Push) {
                val currentKey = oldStack.peekKey()
                val currentViewState = oldStack.obtainViewState(currentKey)
                saveViewState(oldView, currentViewState)
            }
            val upcomingKey = newStack.peekKey()
            val upcomingViewState = newStack.obtainViewState(upcomingKey)
            val context = ViewKeyContextWrapper(activity, upcomingKey)
            val view = upcomingKey.buildView(context)
            restoreViewState(view, upcomingViewState)
            val fadeOutAnim = createFadeOutAnimation(container, oldView)
            oldView.startAnimation(fadeOutAnim)

            container.addView(view)
            view.startAnimation(createFadeInAnimation())
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