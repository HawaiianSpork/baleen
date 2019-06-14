package com.shoprunner.baleen

import java.util.Arrays

class AttributeDescription(
    val dataDescription: DataDescription,
    val name: String,
    val type: BaleenType,
    val markdownDescription: String,
    val aliases: Array<String>,
    val required: Boolean,
    val default: Any?
) {
    fun test(validator: Validator) {
        // TODO change context
        dataDescription.test(validator)
    }

    fun describe(block: (AttributeDescription) -> Unit) {
        block(this)
    }

    override fun toString(): String {
        return "AttributeDescription(dataDescription=$dataDescription, name='$name', type=$type, markdownDescription='$markdownDescription', aliases=${Arrays.toString(aliases)}, required=$required, default=$default)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttributeDescription

        if (dataDescription != other.dataDescription) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (markdownDescription != other.markdownDescription) return false
        if (!aliases.contentEquals(other.aliases)) return false
        if (required != other.required) return false
        if (default != other.default) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dataDescription.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + markdownDescription.hashCode()
        result = 31 * result + aliases.contentHashCode()
        result = 31 * result + required.hashCode()
        result = 31 * result + (default?.hashCode() ?: 0)
        return result
    }
}