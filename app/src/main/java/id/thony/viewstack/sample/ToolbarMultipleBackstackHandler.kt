package id.thony.viewstack.sample

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.view.forEach
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
                val oldStackTop = oldStack.peekKey()
                val topView = this.container.findViewById<View>(oldStackTop.hashCode())
                if (topView != null) {
                    val viewKey = getViewKey(topView)
                    saveViewState(topView, oldStack.obtainViewState(viewKey))
                }

                if (this.container.childCount == 1) {
                    val anim = createFadeOutAnimation(topView, this.container)
                    topView.startAnimation(anim)
                } else {
                    this.container.forEach { it.clearAnimation() }
                    this.container.removeAllViews()
                }

                val upcomingKey = newStack.peekKey()
                val upcomingViewState = newStack.obtainViewState(upcomingKey)
                val view = buildView(upcomingKey).apply {
                    id = upcomingKey.hashCode()
                }
                restoreViewState(view, upcomingViewState)

                container.addView(view)
                view.startAnimation(createFadeInAnimation())
            }
            NavigationCommand.Restore -> {
                this.container.removeAllViews()

                val upcomingKey = newStack.peekKey()
                val upcomingViewState = newStack.obtainViewState(upcomingKey)
                val view = buildView(upcomingKey).apply {
                    id = upcomingKey.hashCode()
                }
                restoreViewState(view, upcomingViewState)

                container.addView(view)
            }
            NavigationCommand.Push -> {
                if (oldStack.peekKey() === newStack.peekKey()) return

                val oldView = this.container.findViewById<View>(oldStack.peekKey().hashCode())
                requireNotNull(oldView)
                val viewKey = getViewKey(oldView)
                saveViewState(oldView, oldStack.obtainViewState(viewKey))

                if (this.container.childCount == 1) {
                    val anim = createPushSlideOutAnimation(oldView, this.container)
                    oldView.startAnimation(anim)
                } else {
                    this.container.forEach { it.clearAnimation() }
                    this.container.removeAllViews()
                }

                val upcomingKey = newStack.peekKey()
                val upcomingViewState = newStack.obtainViewState(upcomingKey)
                val view = buildView(upcomingKey).apply {
                    id = upcomingKey.hashCode()
                }
                restoreViewState(view, upcomingViewState)

                container.addView(view)
                view.startAnimation(createPushSlideInAnimation())
            }
            NavigationCommand.Pop -> {
                if (oldStack.peekKey() === newStack.peekKey()) return

                val oldView = this.container.findViewById<View>(oldStack.peekKey().hashCode())
                requireNotNull(oldView)

                if (this.container.childCount == 1) {
                    val anim = createPopSlideOutAnimation(oldView, this.container)
                    oldView.startAnimation(anim)
                } else {
                    this.container.forEach { it.clearAnimation() }
                    this.container.removeAllViews()
                }


                val upcomingKey = newStack.peekKey()
                val upcomingViewState = newStack.obtainViewState(upcomingKey)
                val view = buildView(upcomingKey).apply {
                    id = upcomingKey.hashCode()
                }
                restoreViewState(view, upcomingViewState)

                container.addView(view)
                view.startAnimation(createPopSlideInAnimation())
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

    private fun createFadeOutAnimation(view: View, container: ViewGroup): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.fade_out).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(anim: Animation) = Unit

                override fun onAnimationEnd(anim: Animation) {
                    container.removeView(view)
                }

                override fun onAnimationRepeat(anim: Animation) = Unit
            })
        }
    }

    private fun createFadeInAnimation(): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.fade_in)
    }

    private fun createPushSlideOutAnimation(view: View, container: ViewGroup): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.push_slide_out).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(anim: Animation) = Unit

                override fun onAnimationEnd(anim: Animation) {
                    container.removeView(view)
                }

                override fun onAnimationRepeat(anim: Animation) = Unit
            })
        }
    }

    private fun createPushSlideInAnimation(): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.push_slide_in)
    }

    private fun createPopSlideOutAnimation(view: View, container: ViewGroup): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.pop_slide_out).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(anim: Animation) = Unit

                override fun onAnimationEnd(anim: Animation) {
                    container.removeView(view)
                }

                override fun onAnimationRepeat(anim: Animation) = Unit
            })
        }
    }

    private fun createPopSlideInAnimation(): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.pop_slide_in)
    }
}