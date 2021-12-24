package net.nhsd.fhir.converter.controller

import ca.uhn.fhir.context.FhirVersionEnum.DSTU3
import ca.uhn.fhir.context.FhirVersionEnum.R4
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import net.nhsd.fhir.converter.service.ConverterService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(ConverterController::class)
class ConverterControllerTest {
    @MockkBean
    private lateinit var converterService: ConverterService

    @Autowired
    private lateinit var mvc: MockMvc

    companion object {
        const val ENDPOINT = "/\$convert"
        private val JSON = MediaType.APPLICATION_JSON
        private val XML = MediaType.APPLICATION_XML
        fun body(result: MvcResult) = result.response.contentAsString
    }

    @Test
    fun `it should convert r4 json to r3 json`() {
        // Given
        val body = "a resource"
        val contentType = "application/fhir+json; fhirVersion=4.0"

        every { converterService.convert(body, JSON, R4, JSON, DSTU3) } returns "foo"

        val request = post(ENDPOINT)
            .contentType(contentType)
            .content(body)

        // When
        mvc.perform(request)
            .andExpect(status().isOk)
            .andExpect { assertThat(body(it)).isEqualTo("foo") }
    }
}
