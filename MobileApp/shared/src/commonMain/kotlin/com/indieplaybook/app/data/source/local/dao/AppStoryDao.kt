package com.indieplaybook.app.data.source.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Upsert
import com.indieplaybook.app.data.source.local.entity.AppStoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppStoryDao {
    @Query("SELECT * FROM app_story WHERE id = :id")
    suspend fun getById(id: String): AppStoryEntity?

    @Query("SELECT * FROM app_story WHERE id = :id")
    fun getByIdFlow(id: String): Flow<AppStoryEntity?>

    @Query("SELECT * FROM app_story")
    fun getAllFlow(): Flow<List<AppStoryEntity>>

    @Query("SELECT * FROM app_story")
    suspend fun getAll(): List<AppStoryEntity>

    @Upsert
    suspend fun upsert(entity: AppStoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<AppStoryEntity>)

    @Query("UPDATE app_story SET is_bookmarked = NOT is_bookmarked WHERE id = :id")
    suspend fun toggleBookmark(id: String)

    @Query("DELETE FROM app_story WHERE id = :id")
    suspend fun deleteById(id: String)

    @Delete
    suspend fun delete(entity: AppStoryEntity)

    @Query("DELETE FROM app_story")
    suspend fun deleteAll()
}
