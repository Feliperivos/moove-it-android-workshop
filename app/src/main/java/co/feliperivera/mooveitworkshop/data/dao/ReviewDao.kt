package co.feliperivera.mooveitworkshop.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import co.feliperivera.mooveitworkshop.data.entities.Review

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReviews(reviews: List<Review>)

    @Query("SELECT * FROM review ORDER BY created_at ASC")
    fun getReviews(): PagingSource<Int, Review>

    @Query("DELETE FROM review")
    suspend fun clearAllReviews()
}