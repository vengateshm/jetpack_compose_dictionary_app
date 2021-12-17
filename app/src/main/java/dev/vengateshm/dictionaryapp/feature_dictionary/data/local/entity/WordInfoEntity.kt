package dev.vengateshm.dictionaryapp.feature_dictionary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.model.Meaning
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.model.WordInfo

@Entity
data class WordInfoEntity(
    @PrimaryKey
    val id: Int? = null,
    val meanings: List<Meaning>,
    val origin: String,
    val phonetic: String,
    val word: String
) {
    fun toWordInfo() = WordInfo(
        meanings = meanings,
        origin = origin,
        phonetic = phonetic,
        word = word
    )
}
