package dev.fathony.viewstack.sample

import android.content.Context
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import dev.fathony.viewstack.DefaultBackstackHandler2

class AnimationDefaultBackstackHandler(private val context: Context, container: ViewGroup) :
    DefaultBackstackHandler2(context, container) {
        
    override fun enterAnimation(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.fade_in)
    }

    override fun exitAnimation(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.fade_out)
    }

    override fun popEnterAnimation(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.pop_slide_in)
    }

    override fun popExitAnimation(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.pop_slide_out)
    }
}
