package net.nhsd.fhir.converter.controller

import ca.uhn.fhir.context.FhirVersionEnum.DSTU3
import ca.uhn.fhir.context.FhirVersionEnum.R4
import net.nhsd.fhir.converter.service.ConverterService
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_XML
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(path = ["/\$convert"])
class ConverterController(
    private val converterService: ConverterService
) {
    @PostMapping(
        consumes = [
            "application/fhir+json; fhirVersion=4.0",
            "application/fhir+json; fhirVersion=3.0",
            "application/fhir+xml; fhirVersion=4.0",
            "application/fhir+xml; fhirVersion=3.0",
        ]
    )

    fun convert(
        @RequestBody resource: String,
        @RequestHeader("Content-Type") contentType: String,
        @RequestHeader("Accept") accept: String
    ): ResponseEntity<String> {

        if (!validateTypeHeader(contentType)) {
            return errorContentType(contentType)
        }
        if (!validateTypeHeader(accept)) {
            return errorAccept(accept)
        }

        val inMediaType = mediaType(contentType)
        val inFhirVersion = fhirVersion(contentType)

        val outMediaType = mediaType(accept)
        val outFhirVersion = fhirVersion(accept)

        val converted = converterService.convert(resource, inMediaType, inFhirVersion, outMediaType, outFhirVersion)

        return ResponseEntity.ok().body(converted)
    }

    companion object {
        private val typeValidatorRe =
            "application/fhir\\+(json|xml);\\s*fhirversion\\s*=\\s*([34]).0(\\s*;.*)?".toRegex()

        private fun validateTypeHeader(s: String): Boolean = typeValidatorRe.matches(s.lowercase())

        private fun fhirVersion(s: String) = if (s.split("=")[1].contains("3.0")) DSTU3 else R4

        private fun mediaType(s: String) = if (s.split(";")[0].contains("xml")) APPLICATION_XML else APPLICATION_JSON

        private fun errorContentType(contentType: String): ResponseEntity<String> = ResponseEntity.badRequest().body("")

        private fun errorAccept(accept: String): ResponseEntity<String> = ResponseEntity.badRequest().body("")
    }

}
