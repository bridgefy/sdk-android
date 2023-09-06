package me.bridgefy.example.android.alerts.util.log

import timber.log.Timber

class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        // add line number
        return super.createStackElementTag(element) + ":" + element.lineNumber
    }
}
