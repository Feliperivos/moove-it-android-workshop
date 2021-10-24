package co.feliperivera.mooveitworkshop.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "state")
data class State(@PrimaryKey val state_key: String, val value: String?)
