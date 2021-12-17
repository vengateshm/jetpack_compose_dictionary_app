package dev.vengateshm.dictionaryapp.feature_dictionary.data.remote.dto


import com.google.gson.annotations.SerializedName
import dev.vengateshm.dictionaryapp.feature_dictionary.data.local.entity.WordInfoEntity
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.model.WordInfo

data class WordInfoDto(
    @SerializedName("meanings")
    val meanings: List<MeaningDto>,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("phonetic")
    val phonetic: String,
    @SerializedName("phonetics")
    val phonetics: List<PhoneticDto>,
    @SerializedName("word")
    val word: String
) {
    fun toWordInfo() = WordInfo(
        meanings = meanings.map { it.toMeaning() },
        origin = origin,
        phonetic = phonetic,
        word = word
    )

    fun toWordInfoEntity() = WordInfoEntity(
        meanings = meanings.map { it.toMeaning() },
        origin = origin,
        phonetic = phonetic,
        word = word
    )
}