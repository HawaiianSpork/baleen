package com.shoprunner.baleen.reflection

import com.shoprunner.baleen.BaleenType
import com.shoprunner.baleen.DataDescription
import com.shoprunner.baleen.types.AllowsNull
import com.shoprunner.baleen.types.BooleanType
import com.shoprunner.baleen.types.DoubleType
import com.shoprunner.baleen.types.FloatType
import com.shoprunner.baleen.types.InstantType
import com.shoprunner.baleen.types.IntType
import com.shoprunner.baleen.types.LongType
import com.shoprunner.baleen.types.StringType
import com.shoprunner.baleen.types.TimestampMillisType
import java.time.Instant
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

fun KClass<*>.dataDescription(typeMapper: TypeMapper = ::defaultTypeMapper) : DataDescription {

    val klass = this
    val description = DataDescription(
        name = klass.simpleName ?: throw IllegalArgumentException("Cannot get data description from anonymous class"),
        nameSpace = klass.namespace,
        markdownDescription = "<missing>"
    )

    klass.memberProperties.forEach { field ->
        description.attr(
            name = field.name,
            type = typeMapper(field.returnType),
            // Type safe langauges require all values to be set or have a default
            // TODO support defaults
            required = true
        )
    }

    return description
}

internal val KClass<*>.namespace
    get() = {
        val qualifiedName = this.qualifiedName
        val simpleName = this.simpleName
        if (qualifiedName == null || simpleName == null) {
            ""
        } else {
            qualifiedName.substring(0, qualifiedName.length.minus(simpleName.length))
        }
    }()

typealias TypeMapper = (KType) -> BaleenType

fun defaultTypeMapper(type: KType): BaleenType =
    recursiveTypeMapper({ recursiveTypeMapper(::defaultTypeMapper, it) }, type)

/**
 * Maps baleen type to type details that are used for XSD.
 */
fun recursiveTypeMapper(typeMapper: TypeMapper, type: KType): BaleenType {
    val baseType =  when (val klass = type.classifier) {
        Boolean::class -> BooleanType()
        Double::class -> DoubleType()
        Float::class -> FloatType()
        Instant::class -> InstantType()
        Int::class -> IntType()
        Long::class -> LongType()
        String::class -> StringType()
        Instant::class -> InstantType()
        LocalDateTime::class -> TimestampMillisType()
        //Iterable::class.isSubclassOf(klass as KClass<*>) -> OccurrencesType()
        is KClass<*> -> klass.dataDescription(typeMapper)
        else -> throw java.lang.IllegalArgumentException("Cannot create data description from $klass")
    }
    return if (type.isMarkedNullable) {
        AllowsNull(baseType)
    } else {
        baseType
    }
}