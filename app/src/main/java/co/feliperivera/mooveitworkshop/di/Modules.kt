package co.feliperivera.mooveitworkshop.di

import android.content.Context
import androidx.room.Room
import co.feliperivera.mooveitworkshop.data.*
import co.feliperivera.mooveitworkshop.data.dao.MovieDao
import co.feliperivera.mooveitworkshop.data.dao.RemoteKeyDao
import co.feliperivera.mooveitworkshop.data.dao.StateDao
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
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) : MyDatabase {
        return Room.databaseBuilder(
            appContext,
            MyDatabase::class.java,
            "mooveitworkshop.db"
        ).build()
    }

    @Provides
    fun provideWebService(okHttpClient: OkHttpClient) : WebService {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WebService::class.java)
    }

    @Provides
    fun provideAuthInterceptorOkHttpClient(
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.NONE

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
    fun provideMovieDao(database: MyDatabase) : MovieDao {
        return database.movieDao()
    }

    @Provides
    fun provideRemoteKeyDao(database: MyDatabase) : RemoteKeyDao {
        return database.remoteKeyDao()
    }

    @Provides
    fun provideStateDao(database: MyDatabase) : StateDao {
        return database.stateDao()
    }

    @Provides
    fun provideReviewDao(database: MyDatabase) : StateDao {
        return database.stateDao()
    }
}