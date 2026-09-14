package com.indieplaybook.app.data

import com.indieplaybook.app.data.source.local.entity.AppStoryEntity
import com.indieplaybook.app.data.source.local.entity.cleanPlaceholder
import com.indieplaybook.app.data.source.local.entity.toModel
import kotlin.test.Test
import kotlin.test.assertEquals

class AppStoryPlaceholderTest {

    @Test
    fun `cleanPlaceholder removes boilerplate origin story text`() {
        val boilerplate = "The origin story defines what motivated the developer to create this app - were they trying to solve their own problem or did they see an underserved niche? Include any details on how the first version was built."
        assertEquals("", cleanPlaceholder(boilerplate))
    }

    @Test
    fun `cleanPlaceholder removes boilerplate growth story text`() {
        val boilerplate = "The growth story tells what strategy this app leverages to gain users and grow revenue. Are they active on socials (TikTok, X, Instagram)? Do they use paid user acquisition, App Store Optimization (ASO), or viral referral loops?"
        assertEquals("", cleanPlaceholder(boilerplate))

        val boilerplateWithGrowth = "The growth story tells what strategy this app leverages to gain users and grow revenue."
        assertEquals("", cleanPlaceholder(boilerplateWithGrowth))
    }

    @Test
    fun `cleanPlaceholder preserves genuine stories`() {
        val genuineOrigin = "Started by passionate travelers wanting beautiful live flight tracking."
        assertEquals(genuineOrigin, cleanPlaceholder(genuineOrigin))

        val genuineGrowth = "Mastered Live Activities and App Store editorial featuring."
        assertEquals(genuineGrowth, cleanPlaceholder(genuineGrowth))
    }

    @Test
    fun `toModel cleans placeholder text on entity`() {
        val entity = AppStoryEntity(
            id = "test-1",
            name = "TestApp",
            oneLiner = "One liner",
            techStack = "KMP",
            originStory = "The origin story defines what motivated the developer...",
            growthPlaybook = "The growh story tells what strategy...",
            iconUrl = "",
            isBookmarked = false,
        )

        val model = entity.toModel()
        assertEquals("", model.originStory)
        assertEquals("", model.growthPlaybook)
    }
}
