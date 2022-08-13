package id.thony.viewstack.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.thony.viewstack.Backstack
import id.thony.viewstack.DefaultBackstackHandler
import id.thony.viewstack.Navigator
import id.thony.viewstack.sample.databinding.ActivityMainBinding
import id.thony.viewstack.sample.formscreen.RegisterKey
import id.thony.viewstack.sample.homescreen.TitleKey
import id.thony.viewstack.sample.listscreen.LeaderboardKey

class MainActivity : AppCompatActivity() {

    companion object {
        const val NavigatorService = "NavigatorService"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnNavigationItemReselectedListener {

        }

        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> navigator.replace(Backstack.of(TitleKey()))
                R.id.list -> navigator.replace(Backstack.of(LeaderboardKey()))
                R.id.form -> navigator.replace(Backstack.of(RegisterKey()))
            }
            return@setOnNavigationItemSelectedListener true
        }

        this.navigator =
            Navigator(DefaultBackstackHandler(this, binding.container), Backstack.of(TitleKey()))
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