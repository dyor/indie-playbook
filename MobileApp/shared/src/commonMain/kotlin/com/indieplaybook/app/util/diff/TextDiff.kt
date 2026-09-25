package com.indieplaybook.app.util.diff

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

object TextDiff {
    fun computeDiffAnnotatedString(original: String, suggested: String): AnnotatedString {
        if (original == suggested) {
            return buildAnnotatedString { append(suggested) }
        }
        if (original.isBlank()) {
            return buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(suggested)
                }
            }
        }
        if (suggested.isBlank()) {
            return buildAnnotatedString {
                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                    append(original)
                }
            }
        }

        val origWords = tokenize(original)
        val suggWords = tokenize(suggested)

        val diffs = computeLcsDiff(origWords, suggWords)

        return buildAnnotatedString {
            for (diff in diffs) {
                when (diff.type) {
                    DiffType.UNCHANGED -> {
                        append(diff.text)
                    }

                    DiffType.DELETED -> {
                        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            append(diff.text)
                        }
                    }

                    DiffType.INSERTED -> {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(diff.text)
                        }
                    }
                }
            }
        }
    }

    private enum class DiffType { UNCHANGED, DELETED, INSERTED }
    private data class DiffItem(val type: DiffType, val text: String)

    private fun tokenize(text: String): List<String> {
        val regex = Regex("(\\s+|\\S+)")
        return regex.findAll(text).map { it.value }.toList()
    }

    private fun computeLcsDiff(orig: List<String>, sugg: List<String>): List<DiffItem> {
        val n = orig.size
        val m = sugg.size
        val dp = Array(n + 1) { IntArray(m + 1) }

        for (i in 0 until n) {
            for (j in 0 until m) {
                if (orig[i] == sugg[j]) {
                    dp[i + 1][j + 1] = dp[i][j] + 1
                } else {
                    dp[i + 1][j + 1] = maxOf(dp[i + 1][j], dp[i][j + 1])
                }
            }
        }

        var i = n
        var j = m

        val rawDiffs = mutableListOf<DiffItem>()
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && orig[i - 1] == sugg[j - 1]) {
                rawDiffs.add(DiffItem(DiffType.UNCHANGED, orig[i - 1]))
                i--
                j--
            } else if (j > 0 && (i == 0 || dp[i][j - 1] >= dp[i - 1][j])) {
                rawDiffs.add(DiffItem(DiffType.INSERTED, sugg[j - 1]))
                j--
            } else if (i > 0 && (j == 0 || dp[i][j - 1] < dp[i - 1][j])) {
                rawDiffs.add(DiffItem(DiffType.DELETED, orig[i - 1]))
                i--
            }
        }

        rawDiffs.reverse()

        val diffItems = mutableListOf<DiffItem>()
        for (item in rawDiffs) {
            if (diffItems.isNotEmpty() && diffItems.last().type == item.type) {
                val last = diffItems.removeAt(diffItems.size - 1)
                diffItems.add(DiffItem(last.type, last.text + item.text))
            } else {
                diffItems.add(item)
            }
        }

        return diffItems
    }
}
