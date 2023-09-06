package me.bridgefy.example.android.alerts.util.log

import timber.log.Timber

/**
 * Non-fatal exception for use with Timber and Crashlytics
 */
class TimberNonFatalCrashLogException(message: String) : RuntimeException(message) {

    override fun fillInStackTrace(): Throwable {
        super.fillInStackTrace()
        val iterator = stackTrace.iterator()
        val filtered = ArrayList<StackTraceElement>()

        // heading to top of Timber stack trace
        while (iterator.hasNext()) {
            val stackTraceElement = iterator.next()
            if (isTimber(stackTraceElement)) {
                break
            }
        }

        // copy all
        var isReachedApp = false
        while (iterator.hasNext()) {
            val stackTraceElement = iterator.next()
            // skip Timber
            if (!isReachedApp && isTimber(stackTraceElement)) {
                continue
            }
            isReachedApp = true
            filtered.add(stackTraceElement)
        }

        stackTrace = filtered.toTypedArray()
        return this
    }

    private fun isTimber(stackTraceElement: StackTraceElement): Boolean {
        return stackTraceElement.className == Timber::class.java.name
    }
}
