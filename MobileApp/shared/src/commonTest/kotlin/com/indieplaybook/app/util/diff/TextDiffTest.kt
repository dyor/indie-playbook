package com.indieplaybook.app.util.diff

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TextDiffTest {

    @Test
    fun testSameStringProducesIdenticalAnnotatedString() {
        val result = TextDiff.computeDiffAnnotatedString("Flutter", "Flutter")
        assertEquals("Flutter", result.text)
    }

    @Test
    fun testEmptyOriginalProducesBoldAdditions() {
        val result = TextDiff.computeDiffAnnotatedString("", "Kotlin Multiplatform")
        assertEquals("Kotlin Multiplatform", result.text)
        assertTrue(result.spanStyles.isNotEmpty())
    }

    @Test
    fun testEmptySuggestedProducesStrikethroughRemovals() {
        val result = TextDiff.computeDiffAnnotatedString("React Native", "")
        assertEquals("React Native", result.text)
        assertTrue(result.spanStyles.isNotEmpty())
    }

    @Test
    fun testDiffComputesInsertionsAndDeletions() {
        val result = TextDiff.computeDiffAnnotatedString("Built with Flutter", "Built with Kotlin Multiplatform")
        assertEquals("Built with FlutterKotlin Multiplatform", result.text)
        assertTrue(result.spanStyles.size >= 2)
    }
}
