package net.nhsd.fhir.converter

import ca.uhn.fhir.context.FhirVersionEnum.DSTU3
import ca.uhn.fhir.context.FhirVersionEnum.R4
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalStateException
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.hl7.fhir.dstu3.model.MedicationRequest as R3MedicationRequest
import org.hl7.fhir.dstu3.model.MedicationStatement as R3MedicationStatement
import org.hl7.fhir.r4.model.MedicationRequest as R4MedicationRequest
import org.hl7.fhir.r4.model.MedicationStatement as R4MedicationStatement

internal class ResourceTypeInferenceTest {
    companion object {
        private const val MEDICATION_REQUEST_JSON = "{\"resourceType\":  \"MedicationRequest\"}"
        private const val MEDICATION_REQUEST_XML = "<MedicationRequest></MedicationRequest>"
        private const val MEDICATION_STATEMENT_JSON = "{\"resourceType\":  \"MedicationStatement\"}"
        private const val MEDICATION_STATEMENT_XML = "<MedicationStatement></MedicationStatement>"

        private val JSON = MediaType.APPLICATION_JSON
        private val XML = MediaType.APPLICATION_XML
    }

    @Test
    internal fun `it should throw when content-type is not supported`() {
        assertThatIllegalStateException().isThrownBy {
            getResourceType(MEDICATION_REQUEST_JSON, MediaType.TEXT_PLAIN, DSTU3)
        }
    }

    @Test
    internal fun `it should throw when resource type is not supported`() {
        val notSupportedRes = "{\"resourceType\": \"Foo\"}"
        assertThatIllegalStateException().isThrownBy {
            getResourceType(notSupportedRes, JSON, DSTU3)
        }
    }

    @Test
    fun `it should return r4 medication request given json input`() {
        // Given
        val resource = getResourceType(MEDICATION_REQUEST_JSON, JSON, R4)

        // Then
        assertThat(resource).isEqualTo(R4MedicationRequest::class.java)
    }

    @Test
    fun `it should return r4 medication request given xml input`() {
        // Given
        val resource = getResourceType(MEDICATION_REQUEST_XML, XML, R4)

        // Then
        assertThat(resource).isEqualTo(R4MedicationRequest::class.java)
    }

    @Test
    fun `it should return r3 medication request given json input`() {
        // Given
        val resource = getResourceType(MEDICATION_REQUEST_JSON, JSON, DSTU3)

        // Then
        assertThat(resource).isEqualTo(R3MedicationRequest::class.java)
    }

    @Test
    fun `it should return r3 medication request given xml input`() {
        // Given
        val resource = getResourceType(MEDICATION_REQUEST_XML, XML, DSTU3)

        // Then
        assertThat(resource).isEqualTo(R3MedicationRequest::class.java)
    }

    @Test
    fun `it should return r4 medication statement given json input`() {
        // Given
        val resource = getResourceType(MEDICATION_STATEMENT_JSON, JSON, R4)

        // Then
        assertThat(resource).isEqualTo(R4MedicationStatement::class.java)
    }

    @Test
    fun `it should return r4 medication statement given xml input`() {
        // Given
        val resource = getResourceType(MEDICATION_STATEMENT_XML, XML, R4)

        // Then
        assertThat(resource).isEqualTo(R4MedicationStatement::class.java)
    }

    @Test
    fun `it should return r3 medication statement given json input`() {
        // Given
        val resource = getResourceType(MEDICATION_STATEMENT_JSON, JSON, DSTU3)

        // Then
        assertThat(resource).isEqualTo(R3MedicationStatement::class.java)
    }

    @Test
    fun `it should return r3 medication statement given xml input`() {
        // Given
        val resource = getResourceType(MEDICATION_STATEMENT_XML, XML, DSTU3)

        // Then
        assertThat(resource).isEqualTo(R3MedicationStatement::class.java)
    }
}
