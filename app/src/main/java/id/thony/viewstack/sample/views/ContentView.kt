package id.thony.viewstack.sample.views

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import id.thony.viewstack.sample.R
import id.thony.viewstack.sample.databinding.ViewContentBinding
import id.thony.viewstack.sample.getKey

class ContentView(context: Context) : FrameLayout(context) {

    private val binding: ViewContentBinding

    init {
        inflate(context, R.layout.view_content, this)
        binding = ViewContentBinding.bind(this)

        val key = getKey<Keys.ContentKey>()
        val content = "Content here with: ${key.detailId} and type: ${key.type}"
        binding.content.text = content
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("ContentView", "onAttachedToWindow")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("ContentView", "onDetachedFromWindow")
    }
}