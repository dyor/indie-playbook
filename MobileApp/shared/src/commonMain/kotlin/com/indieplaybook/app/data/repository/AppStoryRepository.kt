package com.indieplaybook.app.data.repository

import com.indieplaybook.app.data.source.local.dao.AppStoryDao
import com.indieplaybook.app.data.source.local.entity.AppStoryEntity
import com.indieplaybook.app.data.source.local.entity.toModel
import com.indieplaybook.app.domain.model.AppStory
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

    suspend fun seedInitialDataIfEmpty() {
        try {
            val collection = Firebase.firestore.collection("app_stories")
            val snapshot = collection.get()
            val entities = snapshot.documents.map { doc ->
                AppStoryEntity(
                    id = doc.id,
                    name = doc.get<String?>("app_name") ?: doc.get<String?>("name") ?: "",
                    oneLiner = doc.get<String?>("oneLiner") ?: doc.get<String?>("one_liner") ?: "",
                    techStack = doc.get<String?>("framework") ?: doc.get<String?>("tech_stack") ?: doc.get<String?>("techStack") ?: "",
                    originStory = doc.get<String?>("origin_story") ?: doc.get<String?>("originStory") ?: "",
                    growthPlaybook = doc.get<String?>("growth_story") ?: doc.get<String?>("growthPlaybook") ?: doc.get<String?>("growth_playbook") ?: "",
                    iconUrl = doc.get<String?>("image_url") ?: doc.get<String?>("iconUrl") ?: doc.get<String?>("icon_url") ?: "",
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
                    appWebsiteUrl = doc.get<String?>("app_website_url") ?: doc.get<String?>("appWebsiteUrl")
                )
            }
            if (entities.isNotEmpty()) {
                val existingStories = appStoryDao.getAll()
                val hasOldDummyData = existingStories.any { it.id == "1" || it.id == "2" || it.id == "3" || it.id == "4" }
                if (hasOldDummyData) {
                    appStoryDao.deleteAll()
                }
                appStoryDao.insertAll(entities)
            }
        } catch (e: Exception) {
            AppLogger.e("Failed to fetch stories from Firestore", e, "AppStoryRepository")
        }
    }
}
