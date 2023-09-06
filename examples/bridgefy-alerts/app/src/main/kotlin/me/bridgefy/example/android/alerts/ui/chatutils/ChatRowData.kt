package me.bridgefy.example.android.alerts.ui.chatutils

class ChatRowData {

    var text: String = ""
        internal set

    var textWidth: Int = 0
        internal set
    var lastLineWidth: Float = 0f
        internal set
    var lineCount: Int = 0
        internal set

    var rowWidth: Int = 0
        internal set

    var rowHeight: Int = 0
        internal set

    var parentWidth: Int = 0
        internal set

    var measuredType: Int = 0
        internal set

    override fun toString(): String {
        return "ChatRowData text: $text, " +
            "lastLineWidth: $lastLineWidth, lineCount: $lineCount, " +
            "textWidth: $textWidth, rowWidth: $rowWidth, height: $rowHeight, " +
            "parentWidth: $parentWidth, measuredType: $measuredType"
    }
}
