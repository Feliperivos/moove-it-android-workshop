package co.feliperivera.mooveitworkshop.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Movie::class, RemoteKey::class, MovieGenre::class, MoviesGenresRelations::class, State::class],version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun stateDao(): StateDao
}