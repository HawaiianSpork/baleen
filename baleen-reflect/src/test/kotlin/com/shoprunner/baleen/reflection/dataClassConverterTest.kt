package com.shoprunner.baleen.reflection

import com.shoprunner.baleen.AttributeDescription
import com.shoprunner.baleen.NoDefault
import com.shoprunner.baleen.types.IntType
import com.shoprunner.baleen.types.StringType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataClassConverterTest {

    @Test
    fun `given simple data class can create data description`() {
        data class Dog(
            val name: String,
            val legs: Int)

        assertSoftly {
            val desc = Dog::class.dataDescription()
            assertThat(desc.name).isEqualTo("Dog")
            assertThat(desc.nameSpace).isEqualTo("")
            assertThat(desc.attrs).containsExactlyInAnyOrder(
                AttributeDescription(
                    dataDescription = desc,
                    name = "name",
                    type = StringType(),
                    markdownDescription = "",
                    aliases = emptyArray(),
                    required = true,
                    default = NoDefault
                ),
                AttributeDescription(
                    dataDescription = desc,
                    name = "legs",
                    type = IntType(),
                    markdownDescription = "",
                    aliases = emptyArray(),
                    required = true,
                    default = NoDefault
                ))
        }
    }
}