package dev.fathony.viewstack.sample

import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.venom.Venom
import dev.fathony.viewstack.Backstack
import dev.fathony.viewstack.Navigator
import dev.fathony.viewstack.sample.databinding.ActivityMainBinding
import dev.fathony.viewstack.sample.formscreen.RegisterKey
import dev.fathony.viewstack.sample.homescreen.TitleKey
import dev.fathony.viewstack.sample.listscreen.LeaderboardKey


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

    private val venom = Venom.getGlobalInstance()

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                venom.start()
            }
        }

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

//        this.navigator =
//            Navigator(ToolbarMultipleBackstackHandler(this, binding.container), homeBackstack)

        this.navigator =
            Navigator(AnimationDefaultBackstackHandler(this, binding.container), homeBackstack)


        this.navigator.onCreate(savedInstanceState)

        val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

        if (isDebuggable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            venom.start()
        }
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
