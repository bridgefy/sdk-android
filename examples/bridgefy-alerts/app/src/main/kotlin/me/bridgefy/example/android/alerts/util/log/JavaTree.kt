package me.bridgefy.example.android.alerts.util.log

import timber.log.Timber

class JavaTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val logMessage = if (tag != null && tag.isNotEmpty()) {
            "[$tag] $message"
        } else {
            message
        }

        println(logMessage)
    }
}
