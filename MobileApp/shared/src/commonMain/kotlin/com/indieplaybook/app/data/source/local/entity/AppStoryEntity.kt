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
    @ColumnInfo("category") val category: String? = null
)

fun AppStoryEntity.toModel(): AppStory = AppStory(
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
    category = category
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
    category = category
)
