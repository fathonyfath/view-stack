package dev.fathony.viewstack.sample

import android.view.View
import dev.fathony.viewstack.ViewKey
import dev.fathony.viewstack.ViewKeyContextWrapper

inline fun <reified T : ViewKey> View.getKey(): T {
    return ViewKeyContextWrapper.getViewKey(this.context)
}
