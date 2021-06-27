package id.thony.viewstack.sample

import android.view.View
import id.thony.viewstack.ViewKey
import id.thony.viewstack.ViewKeyContextWrapper

inline fun <reified T : ViewKey> View.getKey(): T {
    return ViewKeyContextWrapper.getViewKey(this.context)
}