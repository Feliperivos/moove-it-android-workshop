package co.feliperivera.mooveitworkshop.data.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Review (
    @PrimaryKey
    var id: String = "",
    val author: String,
    val content: String,
    val created_at: String
){
    @Ignore
    var author_details: AuthorDetails? = null
    var author_avatar: String? = null

    data class AuthorDetails(
        val avatar_path: String?
    )
}

