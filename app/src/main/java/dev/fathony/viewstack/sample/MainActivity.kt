package dev.fathony.viewstack.sample

import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.venom.Venom
import dev.fathony.viewstack.Backstack
import dev.fathony.viewstack.Navigator
import dev.fathony.viewstack.WillHandleNavigationChangedListener
import dev.fathony.viewstack.sample.databinding.ActivityMainBinding
import dev.fathony.viewstack.sample.formscreen.RegisterKey
import dev.fathony.viewstack.sample.homescreen.TitleKey
import dev.fathony.viewstack.sample.listscreen.LeaderboardKey


class MainActivity : AppCompatActivity() {

    companion object {
        const val NAVIGATOR_SERVICE = "NavigatorService"

        private const val HOME_BACKSTACK_KEY = "HomeBackstack"
        private const val LIST_BACKSTACK_KEY = "ListBackstack"
        private const val FORM_BACKSTACK_KEY = "FormBackstack"
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

    private val popBackstackBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            navigator.pop()
        }
    }

    private val backToHomeBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            binding.bottomNav.selectedItemId = R.id.home
        }

    }

    private val listener =
        WillHandleNavigationChangedListener(popBackstackBackPressedCallback::isEnabled::set)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreBackstackInstances(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemReselectedListener {

        }

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> navigator.replace(homeBackstack)
                R.id.list -> navigator.replace(listBackstack)
                R.id.form -> navigator.replace(formBackstack)
            }
            backToHomeBackPressedCallback.isEnabled = menuItem.itemId != R.id.home
            return@setOnItemSelectedListener true
        }
//        this.navigator =
//            Navigator(ToolbarMultipleBackstackHandler(this, binding.container), homeBackstack)

        navigator =
            Navigator(AnimationDefaultBackstackHandler(this, binding.container), homeBackstack)
        navigator.onCreate(savedInstanceState)
        
        onBackPressedDispatcher.addCallback(backToHomeBackPressedCallback)
        onBackPressedDispatcher.addCallback(popBackstackBackPressedCallback)

        popBackstackBackPressedCallback.isEnabled = navigator.willHandleNavigation()
        navigator.addWillHandleNavigationChangedListener(listener)

        val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

        if (isDebuggable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                venom.start()
            }
        }

        if (savedInstanceState == null) {
            backToHomeBackPressedCallback.isEnabled = binding.bottomNav.selectedItemId != R.id.home
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        backToHomeBackPressedCallback.isEnabled = binding.bottomNav.selectedItemId != R.id.home
    }

    override fun onDestroy() {
        navigator.removeWillHandleNavigationChangedListener(listener)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(HOME_BACKSTACK_KEY, homeBackstack)
        outState.putParcelable(LIST_BACKSTACK_KEY, listBackstack)
        outState.putParcelable(FORM_BACKSTACK_KEY, formBackstack)

        navigator.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun getSystemService(name: String): Any? {
        val service = super.getSystemService(name)
        if (service != null) {
            return service
        }

        if (name == NAVIGATOR_SERVICE) {
            return navigator
        }

        return null
    }

    override fun onSupportNavigateUp(): Boolean {
        navigator.pop()
        return true
    }

    private fun restoreBackstackInstances(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            homeBackstack = Backstack.of(TitleKey())
            listBackstack = Backstack.of(LeaderboardKey())
            formBackstack = Backstack.of(RegisterKey())
        } else {
            homeBackstack = savedInstanceState.getParcelableCompat(HOME_BACKSTACK_KEY)!!
            listBackstack = savedInstanceState.getParcelableCompat(LIST_BACKSTACK_KEY)!!
            formBackstack = savedInstanceState.getParcelableCompat(FORM_BACKSTACK_KEY)!!

        }
    }
}
