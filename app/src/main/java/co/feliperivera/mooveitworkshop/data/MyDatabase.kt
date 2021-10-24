package co.feliperivera.mooveitworkshop.data

import androidx.room.Database
import androidx.room.RoomDatabase
import co.feliperivera.mooveitworkshop.data.dao.MovieDao
import co.feliperivera.mooveitworkshop.data.dao.RemoteKeyDao
import co.feliperivera.mooveitworkshop.data.dao.ReviewDao
import co.feliperivera.mooveitworkshop.data.dao.StateDao
import co.feliperivera.mooveitworkshop.data.entities.*

@Database(entities = [
    Movie::class,
    RemoteKey::class,
    MovieGenre::class,
    MoviesGenresRelations::class,
    State::class,
    Review::class]
    ,version = 1, exportSchema = false
) abstract class MyDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun stateDao(): StateDao
    abstract fun reviewDao(): ReviewDao
}