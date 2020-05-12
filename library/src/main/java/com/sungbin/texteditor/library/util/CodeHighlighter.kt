package com.sungbin.texteditor.library.util

import android.text.Editable
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import java.util.*

@Suppress("NAME_SHADOWING")
class CodeHighlighter (private var reservedColor: Int,
                       private var numberColor: Int,
                       private var stringColor: Int,
                       private var annotationColor: Int){

    private val data = ArrayList<Highlighter>()
    private inner class Highlighter(var value: String, var color: Int)

    init {
        initHighlightData()
    }

    private fun initHighlightData() {
        val reservedColorData = arrayOf(
            "function",
            "return",
            "var",
            "let",
            "const",
            "if",
            "else",
            "switch",
            "for",
            "while",
            "do",
            "break",
            "continue",
            "case",
            "in",
            "with",
            "true",
            "false",
            "new",
            "null",
            "undefined",
            "typeof",
            "delete",
            "try",
            "catch",
            "finally",
            "prototype",
            "this",
            "super",
            "default"
        )
        for (n in reservedColorData.indices) {
            data.add(
                Highlighter(
                    reservedColorData[n],
                    -1
                )
            )
        }
    }

    fun addReservedWord(word: String?, color: Int) {
        data.add(Highlighter(word!!, color))
    }

    fun addReservedWord(word: String?) {
        data.add(Highlighter(word!!, -1))
    }

    fun removeReservedWord(word: String) {
        var index = -1
        for (n in data.indices) {
            if (data[n].value == word) {
                index = n
            }
        }
        if (index >= 0) data.removeAt(index)
    }

    fun clearReservedWord() {
        for (n in data.indices) {
            data.removeAt(n)
        }
    }

    fun setReservedWordHighlightColor(color: Int) {
        reservedColor = color
    }

    fun setNumberHighlightColor(color: Int) {
        numberColor = color
    }

    fun setStringHighlightColor(color: Int) {
        stringColor = color
    }

    fun setAnnotationHighlightColor(color: Int) {
        annotationColor = color
    }

    fun apply(s: Editable) {
        val str = s.toString()
        if (str.isEmpty()) return
        val spans =
            s.getSpans(0, s.length, ForegroundColorSpan::class.java)
        for (n in spans.indices) {
            s.removeSpan(spans[n])
        }
        var start = 0
        while (start >= 0) {
            val index = str.indexOf("/*", start)
            var end = str.indexOf("*/", index + 2)
            if (index >= 0 && end >= 0) {
                s.setSpan(
                    ForegroundColorSpan(annotationColor),
                    index, end + 2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                end = -5
            }
            start = end + 2
        }
        start = 0
        while (start >= 0) {
            val index = str.indexOf("//", start)
            var end = str.indexOf("\n", index + 1)
            if (index >= 0 && end >= 0) {
                s.setSpan(
                    ForegroundColorSpan(annotationColor),
                    index, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                end = -1
            }
            start = end
        }
        start = 0
        while (start >= 0) {
            var index = str.indexOf("\"", start)
            while (index > 0 && str[index - 1] == '\\') {
                index = str.indexOf("\"", index + 1)
            }
            var end = str.indexOf("\"", index + 1)
            while (end > 0 && str[end - 1] == '\\') {
                end = str.indexOf("\"", end + 1)
            }
            if (index >= 0 && end >= 0) {
                var span = s.getSpans(
                    index,
                    end + 1,
                    ForegroundColorSpan::class.java
                )
                if (span.isNotEmpty()) {
                    if (str.substring(index + 1, end).contains("/*") && str.substring(
                            index + 1,
                            end
                        ).contains("*/")
                    ) {
                        for (n in span.indices) {
                            s.removeSpan(span[n])
                        }
                        s.setSpan(
                            ForegroundColorSpan(stringColor),
                            index, end + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    } else if (str.substring(index + 1, end).contains("//")) {
                        span = s.getSpans(
                            index,
                            str.indexOf("\n", end),
                            ForegroundColorSpan::class.java
                        )
                        for (n in span.indices) {
                            s.removeSpan(span[n])
                        }
                        s.setSpan(
                            ForegroundColorSpan(stringColor),
                            index, end + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                } else {
                    s.setSpan(
                        ForegroundColorSpan(stringColor),
                        index, end + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                end = -5
            }
            start = end + 1
        }
        start = 0
        while (start >= 0) {
            var index = str.indexOf("'", start)
            while (index > 0 && str[index - 1] == '\\') {
                index = str.indexOf("'", index + 1)
            }
            var end = str.indexOf("'", index + 1)
            while (end > 0 && str[end - 1] == '\\') {
                end = str.indexOf("'", end + 1)
            }
            if (index >= 0 && end >= 0) {
                var span = s.getSpans(
                    index,
                    end + 1,
                    ForegroundColorSpan::class.java
                )
                if (span.size > 0) {
                    if (str.substring(index + 1, end).contains("/*") && str.substring(
                            index + 1,
                            end
                        ).contains("*/")
                    ) {
                        for (n in span.indices) {
                            s.removeSpan(span[n])
                        }
                        s.setSpan(
                            ForegroundColorSpan(stringColor),
                            index, end + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    } else if (str.substring(index + 1, end).contains("//")) {
                        span = s.getSpans(
                            index,
                            str.indexOf("\n", end),
                            ForegroundColorSpan::class.java
                        )
                        for (n in span.indices) {
                            s.removeSpan(span[n])
                        }
                        s.setSpan(
                            ForegroundColorSpan(stringColor),
                            index, end + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                } else {
                    s.setSpan(
                        ForegroundColorSpan(stringColor),
                        index, end + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                end = -5
            }
            start = end + 1
        }
        for (n in data.indices) {
            start = 0
            while (start >= 0) {
                val index = str.indexOf(data[n].value, start)
                var end = index + data[n].value.length
                if (index >= 0) {
                    var color = data[n].color
                    if (color == -1) color = reservedColor
                    if (s.getSpans(
                            index,
                            end,
                            ForegroundColorSpan::class.java
                        ).isEmpty() && isSeparated(str, index, end - 1)
                    ) s.setSpan(
                        ForegroundColorSpan(color),
                        index, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    end = -1
                }
                start = end
            }
        }
        val numberColorData =
            arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".")
        for (n in numberColorData.indices) {
            start = 0
            while (start >= 0) {
                val index = str.indexOf(numberColorData[n], start)
                var end = index + 1
                if (index >= 0) {
                    if (s.getSpans(
                            index,
                            end,
                            ForegroundColorSpan::class.java
                        ).isEmpty() && checkNumber(str, index)
                    ) s.setSpan(
                        ForegroundColorSpan(numberColor),
                        index, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    end = -1
                }
                start = end
            }
        }
    }

    private fun checkNumber(str: String, index: Int): Boolean {
        val start = getStartPos(str, index)
        val end = getEndPos(str, index)
        if (str[end - 1] == '.') return false
        return if (start == 0) {
            if (str[start] == '.') false else isNumber(str.substring(start, end))
        } else {
            if (str[start + 1] == '.') false else isNumber(str.substring(start + 1, end))
        }
    }

    private fun isSplitPoint(ch: Char): Boolean {
        return if (ch == '\n') true else " []{}()+-*/%&|!?:;,<>=^~".contains(ch.toString() + "")
    }

    private fun getStartPos(str: String, index: Int): Int {
        var index = index
        while (index >= 0) {
            if (isSplitPoint(str[index])) return index
            index--
        }
        return 0
    }

    private fun getEndPos(str: String, index: Int): Int {
        var index = index
        while (str.length > index) {
            if (isSplitPoint(str[index])) return index
            index++
        }
        return str.length
    }

    private fun isSeparated(str: String, start: Int, end: Int): Boolean {
        var front = false
        val points = " []{}()+-*/%&|!?:;,<>=^~.".toCharArray()
        if (start == 0) {
            front = true
        } else if (str[start - 1] == '\n') {
            front = true
        } else {
            for (n in points.indices) {
                if (str[start - 1] == points[n]) {
                    front = true
                    break
                }
            }
        }
        if (front) {
            try {
                if (str[end + 1] == '\n') {
                    return true
                } else {
                    for (n in points.indices) {
                        if (str[end + 1] == points[n]) return true
                    }
                }
            } catch (e: Exception) {
                return true
            }
        }
        return false
    }

    private fun isNumber(value: String): Boolean {
        return try {
            val a = java.lang.Double.valueOf(value)
            true
        } catch (e: Exception) {
            false
        }
    }
}