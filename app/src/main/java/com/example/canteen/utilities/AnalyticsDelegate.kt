package com.example.canteen.utilities

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

interface AnalyticsDelegate {
    fun registerAnalytics(lifecycle: Lifecycle)
}

class AnalyticsDelegateImpl: AnalyticsDelegate, DefaultLifecycleObserver {
    override fun registerAnalytics(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        traceEvent("Activity started")
    }

    override fun onStop(owner: LifecycleOwner) {
        traceEvent("Activity stopped")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        traceEvent("Activity onDestroy")

    }

    private fun traceEvent(event: String) {
        //MyAnalytics.newEvent(event)
        event.showLog()
    }
}