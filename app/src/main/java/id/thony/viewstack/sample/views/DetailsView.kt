package id.thony.viewstack.sample.views

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.widget.FrameLayout
import id.thony.viewstack.Backstack
import id.thony.viewstack.sample.R
import id.thony.viewstack.sample.databinding.ViewDetailsBinding
import id.thony.viewstack.sample.getKey
import id.thony.viewstack.sample.navigator

class DetailsView(context: Context) : FrameLayout(context) {

    private val binding: ViewDetailsBinding

    init {
        inflate(context, R.layout.view_details, this)
        binding = ViewDetailsBinding.bind(this)
        val key = getKey<Keys.DetailsKey>()
        binding.label.text = key.detailId.toString()
        binding.goToNext.setOnClickListener {
            val currentId = key.detailId
            navigator.push(Keys.DetailsKey(currentId + 1))
        }
        binding.replaceWithContent.setOnClickListener {
            val currentId = key.detailId
            val randomType =
                listOf(Keys.ContentKey.NormalType, Keys.ContentKey.ExtendedType).random()
            navigator.replace(Backstack.of(Keys.ContentKey(currentId, randomType)))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("DetailsView", "onAttachedToWindow")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("DetailsView", "onDetachedFromWindow")
    }
}