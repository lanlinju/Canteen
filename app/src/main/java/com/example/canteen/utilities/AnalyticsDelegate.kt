package com.example.canteen.utilities

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.canteen.activities.MainActivity

interface AnalyticsDelegate {
    fun registerAnalytics(lifecycle: Lifecycle)
}

interface BottomNavigationVisibilityDelegate{
    fun registerBackPressed(fragment:Fragment)
}

class BottomNavigationVisibilityImpl:BottomNavigationVisibilityDelegate,DefaultLifecycleObserver{
    lateinit var mainActivity: MainActivity
    override fun registerBackPressed(fragment: Fragment) {
        fragment.lifecycle.addObserver(this)
        mainActivity = fragment.requireActivity() as MainActivity
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onCreate(owner)
        mainActivity.toggleBottomNavigationVisibility()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        mainActivity.toggleBottomNavigationVisibility()
        super.onDestroy(owner)
    }
}

class AnalyticsDelegateImpl: AnalyticsDelegate, DefaultLifecycleObserver {
    override fun registerAnalytics(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        traceEvent("started")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        traceEvent("resume")
    }

    override fun onStop(owner: LifecycleOwner) {
        traceEvent("stopped")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        traceEvent("onDestroy")

    }

    private fun traceEvent(event: String) {
        //MyAnalytics.newEvent(event)
        "Activity $event".showLog()
    }
}