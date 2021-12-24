package net.nhsd.fhir.converter

import ca.uhn.fhir.context.FhirVersionEnum.DSTU3
import ca.uhn.fhir.parser.IParser
import io.mockk.every
import io.mockk.mockk
import net.nhsd.fhir.converter.model.FhirContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.hl7.fhir.dstu3.model.MedicationRequest as R3MedicationRequest
import org.hl7.fhir.r4.model.MedicationRequest as R4MedicationRequest


internal class FhirParserTest {
    private val r3JsonParser = mockk<IParser>()
    private val r4JsonParser = mockk<IParser>()
    private val r3XmlParser = mockk<IParser>()
    private val r4XmlParser = mockk<IParser>()

    private lateinit var fhirParser: FhirParser

    companion object {
        val JSON = MediaType.APPLICATION_JSON
        val XML = MediaType.APPLICATION_XML

        const val STU3_JSON_RES = "a stu3 json resource"
        const val STU3_XML_RES = "a stu3 xml resource"
        const val R4_JSON_RES = "a r4 json resource"
        const val R4_XML_RES = "a r4 xml resource"

        val A_STU3_RES = R3MedicationRequest()
        val A_R4_RES = R4MedicationRequest()
        val STU3_CLASS = R3MedicationRequest::class.java
        val R4_CLASS = R4MedicationRequest::class.java
    }

    @BeforeEach
    internal fun setUp() {
        fhirParser = FhirParser(
            r3JsonParser = r3JsonParser,
            r4JsonParser = r4JsonParser,
            r3XmlParser = r3XmlParser,
            r4XmlParser = r4XmlParser
        )
    }

    @Test
    fun `it should parse r3 json resource to r3 resource`() {
        // Given
        every { r3JsonParser.parseResource(STU3_CLASS, STU3_JSON_RES) } returns A_STU3_RES
        val fhirContent = FhirContent(STU3_JSON_RES, JSON, DSTU3)

        // When
        val resource = fhirParser.parse(fhirContent)

        // Then
        assertThat(resource).isEqualTo(A_STU3_RES)
    }
}
