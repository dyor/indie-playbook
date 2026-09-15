package com.indieplaybook.app.data.repository

import com.indieplaybook.app.data.source.local.dao.AppStoryDao
import com.indieplaybook.app.data.source.local.entity.AppStoryEntity
import com.indieplaybook.app.data.source.local.entity.cleanPlaceholder
import com.indieplaybook.app.data.source.local.entity.toEntity
import com.indieplaybook.app.data.source.local.entity.toModel
import com.indieplaybook.app.domain.model.AppStory
import com.indieplaybook.app.domain.model.AppStorySuggestion
import com.indieplaybook.app.util.logging.AppLogger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppStoryRepository(
    private val appStoryDao: AppStoryDao,
) {
    fun getAllStories(): Flow<List<AppStory>> = appStoryDao.getAllFlow().map { entities ->
        entities.map { it.toModel() }
    }

    fun getStoryById(id: String): Flow<AppStory?> = appStoryDao.getByIdFlow(id).map { entity ->
        entity?.toModel()
    }

    suspend fun toggleBookmark(id: String) {
        appStoryDao.toggleBookmark(id)
    }

    suspend fun submitSuggestion(suggestion: AppStorySuggestion): Result<Unit> = try {
        val collection = Firebase.firestore.collection("story_suggestions")
        val data = mapOf(
            "story_id" to suggestion.storyId,
            "story_name" to suggestion.storyName,
            "user_id" to suggestion.userId,
            "user_email" to (suggestion.userEmail ?: ""),
            "user_display_name" to (suggestion.userDisplayName ?: ""),
            "timestamp" to suggestion.timestamp,
            "status" to "pending",
            "suggested_name" to (suggestion.suggestedName ?: ""),
            "suggested_one_liner" to (suggestion.suggestedOneLiner ?: ""),
            "suggested_tech_stack" to (suggestion.suggestedTechStack ?: ""),
            "suggested_origin_story" to (suggestion.suggestedOriginStory ?: ""),
            "suggested_growth_playbook" to (suggestion.suggestedGrowthPlaybook ?: ""),
            "suggested_category" to (suggestion.suggestedCategory ?: ""),
            "suggested_publisher" to (suggestion.suggestedPublisher ?: ""),
            "suggested_revenue" to (suggestion.suggestedRevenue ?: ""),
            "suggested_downloads" to (suggestion.suggestedDownloads ?: ""),
            "note" to (suggestion.note ?: "")
        )
        collection.add(data)
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e("Failed to submit suggestion", e, "AppStoryRepository")
        Result.failure(e)
    }

    suspend fun getPendingSuggestions(): Result<List<AppStorySuggestion>> = try {
        val collection = Firebase.firestore.collection("story_suggestions")
        val snapshot = collection.get()
        val list = snapshot.documents.mapNotNull { doc ->
            val status = doc.get<String?>("status") ?: "pending"
            if (status != "pending") return@mapNotNull null
            AppStorySuggestion(
                id = doc.id,
                storyId = doc.get<String?>("story_id") ?: "",
                storyName = doc.get<String?>("story_name") ?: "",
                userId = doc.get<String?>("user_id") ?: "",
                userEmail = doc.get<String?>("user_email"),
                userDisplayName = doc.get<String?>("user_display_name"),
                timestamp = doc.get<Double?>("timestamp")?.toLong() ?: 0L,
                status = status,
                suggestedName = doc.get<String?>("suggested_name"),
                suggestedOneLiner = doc.get<String?>("suggested_one_liner"),
                suggestedTechStack = doc.get<String?>("suggested_tech_stack"),
                suggestedOriginStory = doc.get<String?>("suggested_origin_story"),
                suggestedGrowthPlaybook = doc.get<String?>("suggested_growth_playbook"),
                suggestedCategory = doc.get<String?>("suggested_category"),
                suggestedPublisher = doc.get<String?>("suggested_publisher"),
                suggestedRevenue = doc.get<String?>("suggested_revenue"),
                suggestedDownloads = doc.get<String?>("suggested_downloads"),
                note = doc.get<String?>("note")
            )
        }
        Result.success(list)
    } catch (e: Exception) {
        AppLogger.e("Failed to fetch pending suggestions", e, "AppStoryRepository")
        Result.failure(e)
    }

    suspend fun approveSuggestion(suggestion: AppStorySuggestion, currentStory: AppStory): Result<Unit> = try {
        val updatedStory = currentStory.copy(
            name = suggestion.suggestedName?.takeIf { it.isNotBlank() } ?: currentStory.name,
            oneLiner = suggestion.suggestedOneLiner?.takeIf { it.isNotBlank() } ?: currentStory.oneLiner,
            techStack = suggestion.suggestedTechStack?.takeIf { it.isNotBlank() } ?: currentStory.techStack,
            originStory = suggestion.suggestedOriginStory?.takeIf { it.isNotBlank() } ?: currentStory.originStory,
            growthPlaybook = suggestion.suggestedGrowthPlaybook?.takeIf { it.isNotBlank() } ?: currentStory.growthPlaybook,
            category = suggestion.suggestedCategory?.takeIf { it.isNotBlank() } ?: currentStory.category,
            publisher = suggestion.suggestedPublisher?.takeIf { it.isNotBlank() } ?: currentStory.publisher,
            revenue = suggestion.suggestedRevenue?.takeIf { it.isNotBlank() } ?: currentStory.revenue,
            downloads = suggestion.suggestedDownloads?.takeIf { it.isNotBlank() } ?: currentStory.downloads,
        )
        updateStoryInFirestore(updatedStory)
        appStoryDao.upsert(updatedStory.toEntity())
        Firebase.firestore.collection("story_suggestions").document(suggestion.id).set(mapOf("status" to "approved"), merge = true)
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e("Failed to approve suggestion", e, "AppStoryRepository")
        Result.failure(e)
    }

    suspend fun rejectSuggestion(suggestionId: String): Result<Unit> = try {
        Firebase.firestore.collection("story_suggestions").document(suggestionId).set(mapOf("status" to "rejected"), merge = true)
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e("Failed to reject suggestion", e, "AppStoryRepository")
        Result.failure(e)
    }

    suspend fun updateStoryDirectly(updatedStory: AppStory): Result<Unit> = try {
        updateStoryInFirestore(updatedStory)
        appStoryDao.upsert(updatedStory.toEntity())
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e("Failed to update story directly", e, "AppStoryRepository")
        Result.failure(e)
    }

    private suspend fun updateStoryInFirestore(story: AppStory) {
        val docRef = Firebase.firestore.collection("app_stories").document(story.id)
        val data = mapOf(
            "app_name" to story.name,
            "name" to story.name,
            "oneLiner" to story.oneLiner,
            "one_liner" to story.oneLiner,
            "tech_stack" to story.techStack,
            "techStack" to story.techStack,
            "origin_story" to story.originStory,
            "originStory" to story.originStory,
            "growth_story" to story.growthPlaybook,
            "growthPlaybook" to story.growthPlaybook,
            "category" to (story.category ?: ""),
            "publisher" to (story.publisher ?: ""),
            "revenue" to (story.revenue ?: ""),
            "downloads" to (story.downloads ?: ""),
            "store_presence" to (story.storePresence ?: "Dual Store"),
            "storePresence" to (story.storePresence ?: "Dual Store"),
            "google_play_url" to (story.googlePlayUrl ?: ""),
            "app_store_url" to (story.appStoreUrl ?: ""),
            "icon_url" to story.iconUrl
        )
        docRef.set(data, merge = true)
    }

    suspend fun seedInitialDataIfEmpty(): Result<Unit> = try {
        val collection = Firebase.firestore.collection("app_stories")
        val snapshot = collection.get()
        val entities = snapshot.documents.map { doc ->
            AppStoryEntity(
                id = doc.id,
                name = doc.get<String?>("app_name") ?: doc.get<String?>("name") ?: "",
                oneLiner = doc.get<String?>("oneLiner") ?: doc.get<String?>("one_liner") ?: "",
                techStack = doc.get<String?>("framework") ?: doc.get<String?>("tech_stack") ?: doc.get<String?>("techStack") ?: "",
                originStory = cleanPlaceholder(doc.get<String?>("origin_story") ?: doc.get<String?>("originStory")),
                growthPlaybook = cleanPlaceholder(doc.get<String?>("growth_story") ?: doc.get<String?>("growthPlaybook") ?: doc.get<String?>("growth_playbook")),
                iconUrl = doc.get<String?>("icon_url") ?: doc.get<String?>("image_url") ?: doc.get<String?>("iconUrl") ?: "",
                isBookmarked = false,
                downloads = doc.get<String?>("google_play_downloads") ?: doc.get<String?>("downloads"),
                revenue = doc.get<String?>("revenue"),
                publisher = doc.get<String?>("publisher"),
                releaseDate = doc.get<String?>("release_date") ?: doc.get<String?>("releaseDate"),
                category = doc.get<String?>("category"),
                googlePlayUrl = doc.get<String?>("google_play_url") ?: doc.get<String?>("googlePlayUrl"),
                appStoreUrl = doc.get<String?>("app_store_url") ?: doc.get<String?>("appStoreUrl"),
                googlePlayReviews = doc.get<String?>("google_play_reviews") ?: doc.get<String?>("googlePlayReviews"),
                googlePlayRating = doc.get<Double?>("google_play_rating") ?: doc.get<Double?>("googlePlayRating"),
                googlePlayAbout = doc.get<String?>("google_play_about") ?: doc.get<String?>("googlePlayAbout"),
                appStoreRating = doc.get<Double?>("app_store_rating") ?: doc.get<Double?>("appStoreRating"),
                appStoreReviews = doc.get<Int?>("app_store_reviews") ?: doc.get<Int?>("appStoreReviews"),
                appStoreSize = doc.get<String?>("app_store_size") ?: doc.get<String?>("appStoreSize"),
                appStoreChartRank = doc.get<Int?>("app_store_chart_rank") ?: doc.get<Int?>("appStoreChartRank"),
                appStoreChartCategory = doc.get<String?>("app_store_chart_category") ?: doc.get<String?>("appStoreChartCategory"),
                appStoreAbout = doc.get<String?>("app_store_about") ?: doc.get<String?>("appStoreAbout"),
                appStoreAgeRating = doc.get<String?>("app_store_age_rating") ?: doc.get<String?>("appStoreAgeRating"),
                appStorePrivacyPolicy = doc.get<String?>("app_store_privacy_policy") ?: doc.get<String?>("appStorePrivacyPolicy"),
                googlePlayPrivacyPolicy = doc.get<String?>("google_play_privacy_policy") ?: doc.get<String?>("googlePlayPrivacyPolicy"),
                appWebsiteUrl = doc.get<String?>("app_website_url") ?: doc.get<String?>("appWebsiteUrl"),
                storePresence = doc.get<String?>("store_presence") ?: doc.get<String?>("storePresence")
            )
        }
        if (entities.isNotEmpty()) {
            val existingStories = appStoryDao.getAll()
            val bookmarks = existingStories.filter { it.isBookmarked }.map { it.name }.toSet()

            // Delete any existing/duplicate local entries so Room cleanly mirrors Firestore
            appStoryDao.deleteAll()

            val entitiesWithBookmarks = entities.map { entity ->
                val storePres = entity.storePresence ?: if (!entity.googlePlayUrl.isNullOrBlank() && !entity.appStoreUrl.isNullOrBlank()) "Dual Store" else "App Store Only"
                val isBookmarked = bookmarks.contains(entity.name)
                entity.copy(isBookmarked = isBookmarked, storePresence = storePres)
            }
            appStoryDao.insertAll(entitiesWithBookmarks)
        }
        Result.success(Unit)
    } catch (e: Exception) {
        AppLogger.e("Failed to fetch stories from Firestore", e, "AppStoryRepository")
        Result.failure(Exception("Cannot connect to Firestore"))
    }
}
