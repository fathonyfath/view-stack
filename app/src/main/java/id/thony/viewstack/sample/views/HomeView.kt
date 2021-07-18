package id.thony.viewstack.sample.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import id.thony.viewstack.sample.R
import id.thony.viewstack.sample.databinding.ViewHomeBinding
import id.thony.viewstack.sample.navigator

class HomeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewHomeBinding

    init {
        inflate(context, R.layout.view_home, this)
        binding = ViewHomeBinding.bind(this)
        binding.increment.setOnClickListener { increment() }
        binding.decrement.setOnClickListener { decrement() }
        binding.goDetails.setOnClickListener {
            val valueText = binding.text.text
            val intValue = valueText.toString().toIntOrNull() ?: 0
            navigator.push(Keys.DetailsKey(intValue))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("HomeView", "onAttachedToWindow")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("HomeView", "onDetachedFromWindow")
    }

    private fun increment() {
        val valueText = binding.text.text
        val intValue = valueText.toString().toIntOrNull() ?: 0
        binding.text.setText((intValue + 1).toString(), TextView.BufferType.NORMAL)
    }

    private fun decrement() {
        val valueText = binding.text.text
        val intValue = valueText.toString().toIntOrNull() ?: 0
        binding.text.setText((intValue - 1).toString(), TextView.BufferType.NORMAL)
    }
}