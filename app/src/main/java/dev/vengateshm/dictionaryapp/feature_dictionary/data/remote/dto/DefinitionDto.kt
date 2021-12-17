package dev.vengateshm.dictionaryapp.feature_dictionary.data.remote.dto


import com.google.gson.annotations.SerializedName
import dev.vengateshm.dictionaryapp.feature_dictionary.domain.model.Definition

data class DefinitionDto(
    @SerializedName("antonyms")
    val antonyms: List<String>,
    @SerializedName("definition")
    val definition: String,
    @SerializedName("example")
    val example: String?,
    @SerializedName("synonyms")
    val synonyms: List<String>
) {
    fun toDefinition() = Definition(
        antonyms = antonyms,
        definition = definition,
        example = example,
        synonyms = synonyms
    )
}