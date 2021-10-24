package co.feliperivera.mooveitworkshop.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(state: State)

    @Query("SELECT value FROM state WHERE state_key = :query")
    suspend fun stateByQuery(query: String): String

    @Query("DELETE FROM state WHERE state_key = :query")
    suspend fun deleteByQuery(query: String)
}