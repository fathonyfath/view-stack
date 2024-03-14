package dev.fathony.viewstack.sample

import android.annotation.SuppressLint
import android.view.View
import dev.fathony.viewstack.Navigator

val View.navigator: Navigator
    @SuppressLint("WrongConstant")
    get() {
        return context.getSystemService(MainActivity.NavigatorService) as? Navigator
            ?: throw IllegalStateException()
    }
