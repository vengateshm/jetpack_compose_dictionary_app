package dev.vengateshm.dictionaryapp.feature_dictionary.domain.repository

import dev.vengateshm.dictionaryapp.core.util.Resource
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {
    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>
}