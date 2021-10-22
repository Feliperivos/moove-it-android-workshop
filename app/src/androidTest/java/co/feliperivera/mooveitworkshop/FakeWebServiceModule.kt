package co.feliperivera.mooveitworkshop

import co.feliperivera.mooveitworkshop.data.WebService
import co.feliperivera.mooveitworkshop.di.TestModules
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TestModules::class]
)
class FakeWebServiceModule {

    @Provides
    fun provideWebService(okHttpClient: OkHttpClient) : WebService {
        val movieFactory = MovieFactory()
        val mockWebService = MockWebService()
        mockWebService.addMovies(movieFactory.createMovie())
        return mockWebService
    }
}