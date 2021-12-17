package dev.vengateshm.dictionaryapp.feature_dictionary.data

import dev.vengateshm.dictionaryapp.core.util.Resource
import dev.vengateshm.dictionaryapp.feature_dictionary.data.local.WordInfoDao
import dev.vengateshm.dictionaryapp.feature_dictionary.data.remote.DictionaryApi
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.model.WordInfo
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val dictionaryApi: DictionaryApi,
    private val wordInfoDao: WordInfoDao
) : WordInfoRepository {
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())

        val wordInfos = wordInfoDao.getWordInfos(word = word).map { it.toWordInfo() }
        emit(Resource.Loading(wordInfos)) // Emit cache data

        try {
            val remoteWordInfos = dictionaryApi.getWordInfo(word = word)
            wordInfoDao.deleteWordInfos(remoteWordInfos.map { it.word })
            wordInfoDao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!",
                    data = wordInfos
                )
            ) // Cache data
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = wordInfos
                )
            ) // Cache data
        }

        val newWordInfos = wordInfoDao.getWordInfos(word = word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfos))
    }
}