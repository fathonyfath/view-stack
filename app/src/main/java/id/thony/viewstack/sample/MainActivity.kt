package id.thony.viewstack.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import id.thony.viewstack.Navigator
import id.thony.viewstack.Backstack
import id.thony.viewstack.DefaultBackstackHandler
import id.thony.viewstack.sample.views.Keys

class MainActivity : AppCompatActivity() {

    companion object {
        const val NavigatorService = "NavigatorService";
    }

    private lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val content = findViewById<ViewGroup>(R.id.content)
        this.navigator =
            Navigator(DefaultBackstackHandler(this, content), Backstack.of(Keys.HomeKey()))
        this.navigator.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        this.navigator.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.navigator.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        this.navigator.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (!this.navigator.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun getSystemService(name: String): Any? {
        val service = super.getSystemService(name)
        if (service != null) {
            return service
        }

        if (name == NavigatorService) {
            return navigator
        }

        return null
    }
}