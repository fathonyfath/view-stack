package id.thony.viewstack.sample.views

import android.content.Context
import androidx.annotation.IntDef
import id.thony.viewstack.ViewKey
import kotlinx.parcelize.Parcelize

sealed class Keys : ViewKey() {
    @Parcelize
    class HomeKey : Keys() {
        override fun buildView(context: Context) = HomeView(context)
    }

    @Parcelize
    data class DetailsKey(val detailId: Int) : Keys() {
        override fun buildView(context: Context) = DetailsView(context)
    }

    @Parcelize
    class ContentKey(val detailId: Int, @Type val type: Int) : Keys() {
        @IntDef(value = [NormalType, ExtendedType])
        @Retention(AnnotationRetention.SOURCE)
        annotation class Type
        companion object {
            const val NormalType = 0
            const val ExtendedType = 1
        }

        override fun buildView(context: Context) = ContentView(context)
    }
}