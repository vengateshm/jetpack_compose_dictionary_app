package dev.vengateshm.dictionaryapp.feature_dictionary.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.vengateshm.dictionaryapp.feature_dictionary.data.WordInfoRepositoryImpl
import dev.vengateshm.dictionaryapp.feature_dictionary.data.local.Converters
import dev.vengateshm.dictionaryapp.feature_dictionary.data.local.WordInfoDatabase
import dev.vengateshm.dictionaryapp.feature_dictionary.data.remote.DictionaryApi
import dev.vengateshm.dictionaryapp.feature_dictionary.data.util.GsonParser
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.repository.WordInfoRepository
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.use_case.GetWordInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCase(wordInfoRepository: WordInfoRepository): GetWordInfo {
        return GetWordInfo(wordInfoRepository)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(
        api: DictionaryApi,
        db: WordInfoDatabase
    ): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.wordInfoDao)
    }

    @Provides
    @Singleton
    fun provideWordInfoDatabase(application: Application): WordInfoDatabase {
        return Room.databaseBuilder(
            application,
            WordInfoDatabase::class.java,
            "word_db"
        )
            .addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryApi {
        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }
}