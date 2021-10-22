package co.feliperivera.mooveitworkshop.di

import android.content.Context
import androidx.room.Room
import co.feliperivera.mooveitworkshop.data.MovieDao
import co.feliperivera.mooveitworkshop.data.MyDatabase
import co.feliperivera.mooveitworkshop.data.RemoteKeyDao
import co.feliperivera.mooveitworkshop.data.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Modules {

    @Provides
    fun provideAuthInterceptorOkHttpClient(
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor();
        logging.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url()
                val url =
                    originalHttpUrl.newBuilder().addQueryParameter("api_key", "11fd1064a95b26a0991178e2df0265b6")
                        .build()
                request.url(url)
                val response = chain.proceed(request.build())
                return@addInterceptor response
            }
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) : MyDatabase {
        return Room.databaseBuilder(
            appContext,
            MyDatabase::class.java,
            "mooveitworkshop.db"
        ).build()
    }

    @Provides
    fun provideMovieDao(database: MyDatabase) : MovieDao {
        return database.movieDao()
    }

    @Provides
    fun provideRemoteKeyDao(database: MyDatabase) : RemoteKeyDao {
        return database.remoteKeyDao()
    }
}