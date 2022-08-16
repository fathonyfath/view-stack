package id.thony.viewstack.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.thony.viewstack.Backstack
import id.thony.viewstack.Navigator
import id.thony.viewstack.sample.databinding.ActivityMainBinding
import id.thony.viewstack.sample.formscreen.RegisterKey
import id.thony.viewstack.sample.homescreen.TitleKey
import id.thony.viewstack.sample.listscreen.LeaderboardKey

class MainActivity : AppCompatActivity() {

    companion object {
        const val NavigatorService = "NavigatorService"

        private const val HomeBackstackKey = "HomeBackstack"
        private const val ListBackstackKey = "ListBackstack"
        private const val FormBackstackKey = "FormBackstack"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: Navigator

    private lateinit var homeBackstack: Backstack
    private lateinit var listBackstack: Backstack
    private lateinit var formBackstack: Backstack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreBackstackInstances(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnNavigationItemReselectedListener {

        }

        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> navigator.replace(homeBackstack)
                R.id.list -> navigator.replace(listBackstack)
                R.id.form -> navigator.replace(formBackstack)
            }
            return@setOnNavigationItemSelectedListener true
        }

        this.navigator =
            Navigator(ToolbarMultipleBackstackHandler(this, binding.container), homeBackstack)
        this.navigator.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(HomeBackstackKey, homeBackstack)
        outState.putParcelable(ListBackstackKey, listBackstack)
        outState.putParcelable(FormBackstackKey, formBackstack)

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
            if (binding.bottomNav.selectedItemId != R.id.home) {
                binding.bottomNav.selectedItemId = R.id.home
            } else {
                super.onBackPressed()
            }
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

    override fun onSupportNavigateUp(): Boolean {
        this.navigator.onBackPressed()
        return true
    }

    private fun restoreBackstackInstances(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            homeBackstack = Backstack.of(TitleKey())
            listBackstack = Backstack.of(LeaderboardKey())
            formBackstack = Backstack.of(RegisterKey())
        } else {
            homeBackstack = savedInstanceState.getParcelable(HomeBackstackKey)!!
            listBackstack = savedInstanceState.getParcelable(ListBackstackKey)!!
            formBackstack = savedInstanceState.getParcelable(FormBackstackKey)!!

        }
    }
}