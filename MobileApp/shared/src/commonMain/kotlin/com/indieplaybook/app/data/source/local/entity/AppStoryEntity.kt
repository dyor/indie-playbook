@file:OptIn(ExperimentalUuidApi::class)

package com.indieplaybook.app.data.source.local.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.indieplaybook.app.domain.model.AppStory
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "app_story")
data class AppStoryEntity(
    @PrimaryKey @ColumnInfo("id") val id: String = Uuid.random().toString(),
    @ColumnInfo("name") val name: String,
    @ColumnInfo("one_liner") val oneLiner: String,
    @ColumnInfo("tech_stack") val techStack: String,
    @ColumnInfo("origin_story") val originStory: String,
    @ColumnInfo("growth_playbook") val growthPlaybook: String,
    @ColumnInfo("icon_url") val iconUrl: String,
    @ColumnInfo("is_bookmarked") val isBookmarked: Boolean,
    @ColumnInfo("downloads") val downloads: String? = null,
    @ColumnInfo("revenue") val revenue: String? = null,
    @ColumnInfo("publisher") val publisher: String? = null,
    @ColumnInfo("release_date") val releaseDate: String? = null,
    @ColumnInfo("category") val category: String? = null,

    // Store & details fields
    @ColumnInfo("google_play_url") val googlePlayUrl: String? = null,
    @ColumnInfo("app_store_url") val appStoreUrl: String? = null,
    @ColumnInfo("google_play_reviews") val googlePlayReviews: String? = null,
    @ColumnInfo("google_play_rating") val googlePlayRating: Double? = null,
    @ColumnInfo("google_play_about") val googlePlayAbout: String? = null,
    @ColumnInfo("app_store_rating") val appStoreRating: Double? = null,
    @ColumnInfo("app_store_reviews") val appStoreReviews: Int? = null,
    @ColumnInfo("app_store_size") val appStoreSize: String? = null,
    @ColumnInfo("app_store_chart_rank") val appStoreChartRank: Int? = null,
    @ColumnInfo("app_store_chart_category") val appStoreChartCategory: String? = null,
    @ColumnInfo("app_store_about") val appStoreAbout: String? = null,
    @ColumnInfo("app_store_age_rating") val appStoreAgeRating: String? = null,
    @ColumnInfo("app_store_privacy_policy") val appStorePrivacyPolicy: String? = null,
    @ColumnInfo("google_play_privacy_policy") val googlePlayPrivacyPolicy: String? = null,
    @ColumnInfo("app_website_url") val appWebsiteUrl: String? = null,
    @ColumnInfo("store_presence") val storePresence: String? = null,
)

fun unescapeHtml(text: String?): String {
    if (text == null) return ""
    return text
        .replace("&#39;", "'")
        .replace("&apos;", "'")
        .replace("&amp;", "&")
        .replace("&quot;", "\"")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&nbsp;", " ")
        .trim()
}

fun cleanPlaceholder(text: String?): String {
    if (text == null) return ""
    val cleaned = unescapeHtml(text)
    if (cleaned.startsWith("The origin story defines", ignoreCase = true) ||
        cleaned.startsWith("The growh story tells", ignoreCase = true) ||
        cleaned.startsWith("The growth story tells", ignoreCase = true)
    ) {
        return ""
    }
    return cleaned
}

fun AppStoryEntity.toModel(): AppStory = AppStory(
    id = id,
    name = unescapeHtml(name),
    oneLiner = unescapeHtml(oneLiner),
    techStack = unescapeHtml(techStack),
    originStory = cleanPlaceholder(originStory),
    growthPlaybook = cleanPlaceholder(growthPlaybook),
    iconUrl = iconUrl,
    isBookmarked = isBookmarked,
    downloads = downloads,
    revenue = revenue,
    publisher = unescapeHtml(publisher),
    releaseDate = releaseDate,
    category = category,
    googlePlayUrl = googlePlayUrl,
    appStoreUrl = appStoreUrl,
    googlePlayReviews = googlePlayReviews,
    googlePlayRating = googlePlayRating,
    googlePlayAbout = googlePlayAbout,
    appStoreRating = appStoreRating,
    appStoreReviews = appStoreReviews,
    appStoreSize = appStoreSize,
    appStoreChartRank = appStoreChartRank,
    appStoreChartCategory = appStoreChartCategory,
    appStoreAbout = appStoreAbout,
    appStoreAgeRating = appStoreAgeRating,
    appStorePrivacyPolicy = appStorePrivacyPolicy,
    googlePlayPrivacyPolicy = googlePlayPrivacyPolicy,
    appWebsiteUrl = appWebsiteUrl,
    storePresence = storePresence ?: if (!googlePlayUrl.isNullOrBlank() && !appStoreUrl.isNullOrBlank()) "Dual Store" else "App Store Only",
)

fun AppStory.toEntity(): AppStoryEntity = AppStoryEntity(
    id = id,
    name = name,
    oneLiner = oneLiner,
    techStack = techStack,
    originStory = originStory,
    growthPlaybook = growthPlaybook,
    iconUrl = iconUrl,
    isBookmarked = isBookmarked,
    downloads = downloads,
    revenue = revenue,
    publisher = publisher,
    releaseDate = releaseDate,
    category = category,
    googlePlayUrl = googlePlayUrl,
    appStoreUrl = appStoreUrl,
    googlePlayReviews = googlePlayReviews,
    googlePlayRating = googlePlayRating,
    googlePlayAbout = googlePlayAbout,
    appStoreRating = appStoreRating,
    appStoreReviews = appStoreReviews,
    appStoreSize = appStoreSize,
    appStoreChartRank = appStoreChartRank,
    appStoreChartCategory = appStoreChartCategory,
    appStoreAbout = appStoreAbout,
    appStoreAgeRating = appStoreAgeRating,
    appStorePrivacyPolicy = appStorePrivacyPolicy,
    googlePlayPrivacyPolicy = googlePlayPrivacyPolicy,
    appWebsiteUrl = appWebsiteUrl,
    storePresence = storePresence,
)
